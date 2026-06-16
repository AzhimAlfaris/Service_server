# Verdant Flow Service Server Documentation

Dokumen ini menjelaskan kondisi project terbaru untuk backend monitoring sensor ESP32 berbasis Spring Boot.

Project terdiri dari dua service utama:

- `microcontroller-service`
- `application-service`

Selain itu, repo ini juga sudah memiliki:

- `docker-compose.yml` untuk menjalankan seluruh stack
- `init/microcontroller_service_db.sql` untuk inisialisasi database
- `prometheus.yml` untuk scraping metrics

## 1. Ringkasan Cepat

### Build dan test

```powershell
cd microcontroller-service
.\mvnw.cmd test
```

```powershell
cd application-service
.\mvnw.cmd test
```

### Jalankan lokal

```powershell
cd microcontroller-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd application-service
.\mvnw.cmd spring-boot:run
```

### Jalankan stack Docker

```powershell
docker compose up -d --build
```

Jika ingin reset database container:

```powershell
docker compose down -v
docker compose up -d --build
```

## 2. Gambaran Umum

Project ini adalah backend untuk monitoring sensor berbasis ESP32.

Alur utamanya:

1. ESP32 mengirim data sensor ke `microcontroller-service`.
2. `microcontroller-service` menyimpan data ke MySQL.
3. Setelah data tersimpan, service ini mem-publish event notifikasi ke RabbitMQ.
4. `application-service` menerima event tersebut dan mengirim email ke alamat user pada data sensor.
5. Saat aplikasi meminta data terbaru atau riwayat sensor, `application-service` mengirim request ke RabbitMQ dan meneruskan hasilnya ke aplikasi.

## 3. Arsitektur

### 3.1 `microcontroller-service`

Tanggung jawab:

- menerima data sensor dari ESP32
- menyimpan data ke MySQL
- menyediakan endpoint `latest` dan `history` untuk debug langsung
- mengirim event notifikasi ke RabbitMQ setelah insert berhasil

### 3.2 `application-service`

Tanggung jawab:

- menyediakan endpoint untuk aplikasi
- mengirim request data `LATEST` atau `HISTORY` melalui RabbitMQ
- menerima event notifikasi dari RabbitMQ
- mengirim email otomatis ke user
- tidak memakai database sendiri

### 3.3 Komponen Infrastruktur

- MySQL untuk `microcontroller-service`
- RabbitMQ sebagai message broker
- Gmail SMTP untuk email notifikasi
- Prometheus dan Grafana untuk observability

## 4. Teknologi

### 4.1 Stack utama

- Java 17
- Spring Boot 4.1.0
- Spring Web
- Spring AMQP
- Spring Mail
- Spring Data JPA
- Lombok
- Jackson Databind

### 4.2 Observability

Kedua service sudah mengaktifkan:

- `spring-boot-starter-actuator`
- `micrometer-registry-prometheus`

Endpoint yang diekspos:

- `/actuator/health`
- `/actuator/info`
- `/actuator/prometheus`

## 5. Struktur Project

### 5.1 `application-service`

- `controller`
  - `SensorDataController`
- `service`
  - `SensorDataClientService`
  - `SensorEmailListener`
  - `SensorEmailService`
- `config`
  - `RabbitMQConfig`
- `dto`
  - `SensorQueryRequest`
  - `SensorQueryResponse`
  - `SensorReadingResponse`
- `exception`
  - `GlobalExceptionHandler`
  - `ResourceNotFoundException`

### 5.2 `microcontroller-service`

- `controller`
  - `SensorReadingController`
- `service`
  - `SensorReadingService`
  - `SensorRequestListener`
- `config`
  - `RabbitMQConfig`
- `repository`
  - `SensorReadingRepository`
- `model`
  - `SensorReading`
- `dto`
  - `SensorReadingRequest`
  - `SensorReadingResponse`
  - `SensorQueryRequest`
  - `SensorQueryResponse`
- `exception`
  - `GlobalExceptionHandler`
  - `ResourceNotFoundException`

## 6. Konfigurasi Runtime

### 6.1 Port default

- `microcontroller-service` -> `8081`
- `application-service` -> `8082`

### 6.2 `application-service/src/main/resources/application.properties`

Nilai penting yang saat ini dipakai:

- `spring.rabbitmq.host=${RABBITMQ_HOST:localhost}`
- `spring.rabbitmq.port=${RABBITMQ_PORT:5672}`
- `spring.rabbitmq.username=${RABBITMQ_USERNAME:user}`
- `spring.rabbitmq.password=${RABBITMQ_PASSWORD:password}`
- `app.rabbitmq.exchange=sensor.exchange`
- `app.rabbitmq.queue=sensor.request.queue`
- `app.rabbitmq.routing-key=sensor.request`
- `app.rabbitmq.notification-queue=sensor.notification.queue`
- `app.rabbitmq.notification-routing-key=sensor.notification`
- `app.mail.from=${MAIL_FROM:anasrudi048@gmail.com}`
- `spring.mail.host=${MAIL_HOST:smtp.gmail.com}`
- `spring.mail.port=${MAIL_PORT:587}`
- `spring.mail.username=${MAIL_USERNAME:anasrudi048@gmail.com}`
- `spring.mail.password=${MAIL_PASSWORD:...}`

Catatan:

- `SensorEmailListener` aktif hanya jika properti `spring.mail.host` tersedia.
- Jika `MAIL_PASSWORD` tidak diisi dengan App Password Gmail, email tidak akan terkirim.

### 6.3 `microcontroller-service/src/main/resources/application.properties`

Nilai penting yang saat ini dipakai:

- `spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME_MICRO:microcontroller_service_db}`
- `spring.datasource.username=${DB_USERNAME:root}`
- `spring.datasource.password=${DB_PASSWORD:root}`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.rabbitmq.host=${RABBITMQ_HOST:localhost}`
- `spring.rabbitmq.port=${RABBITMQ_PORT:5672}`
- `spring.rabbitmq.username=${RABBITMQ_USERNAME:user}`
- `spring.rabbitmq.password=${RABBITMQ_PASSWORD:password}`
- `app.rabbitmq.exchange=sensor.exchange`
- `app.rabbitmq.queue=sensor.request.queue`
- `app.rabbitmq.routing-key=sensor.request`
- `app.rabbitmq.notification-routing-key=sensor.notification`

## 7. Alur Data

### 7.1 Request data dari aplikasi

1. Aplikasi memanggil endpoint `application-service`.
2. `SensorDataClientService` mengubah request menjadi JSON `SensorQueryRequest`.
3. Payload dikirim ke exchange `sensor.exchange` dengan routing key `sensor.request`.
4. `SensorRequestListener` di `microcontroller-service` menerima payload lewat queue `sensor.request.queue`.
5. Service membaca data dari database.
6. Response dikembalikan sebagai `SensorQueryResponse`.
7. `application-service` meneruskan response ke caller.

### 7.2 Notifikasi email

1. ESP32 mengirim data ke `microcontroller-service`.
2. `SensorReadingService` menyimpan data ke MySQL.
3. Setelah insert berhasil, service mem-publish payload ke routing key `sensor.notification`.
4. `SensorEmailListener` di `application-service` menerima payload dari queue `sensor.notification.queue`.
5. `SensorEmailService` mengirim email ke alamat yang ada di data sensor.

## 8. RabbitMQ Design

### 8.1 Exchange

- `sensor.exchange`

### 8.2 Queue

- `sensor.request.queue`
- `sensor.notification.queue`

### 8.3 Routing key

- `sensor.request`
- `sensor.notification`

### 8.4 Perilaku penting

- Exchange yang dipakai adalah `DirectExchange`.
- Request history memakai `requestType=HISTORY` dan `limit`.
- Request latest memakai `requestType=LATEST`.
- Jika request type tidak ada, listener di `microcontroller-service` default ke `LATEST`.
- Jika response RabbitMQ kosong, `application-service` melempar `ResourceNotFoundException`.

## 9. Database

### 9.1 Database `microcontroller_service_db`

Database hanya dipakai oleh `microcontroller-service`.

Tabel utama:

- `sensor_readings`

Kolom inti:

- `id`
- `microcontroller_id`
- `email`
- `sensor_value`
- `moisture_percent`
- `soil_condition`
- `action`
- `pump_duration`
- `timestamp_sensor`
- `created_at`

### 9.2 Skema SQL inti

```sql
CREATE TABLE sensor_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    microcontroller_id VARCHAR(100) NOT NULL,
    moisture_percent VARCHAR(255) NOT NULL,
    pump_duration VARCHAR(255) NOT NULL,
    sensor_value VARCHAR(255) NOT NULL,
    soil_condition VARCHAR(255) NOT NULL,
    timestamp_sensor VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);
```

File `init/microcontroller_service_db.sql` sudah disiapkan untuk inisialisasi tabel tersebut saat container MySQL dibuat.

### 9.3 `application-service`

Service ini tidak memakai database.

## 10. Endpoint API

## 10.1 `microcontroller-service`

Base URL:

```text
http://localhost:8081
```

### POST `/api/sensor-readings`

Simpan data sensor baru.

Contoh request:

```json
{
  "microcontrollerId": "ESP32-001",
  "email": "user@example.com",
  "sensorValue": "742",
  "moisturePercent": "68",
  "soilCondition": "Normal",
  "action": "Pump ON",
  "pumpDuration": "5 seconds",
  "timestampSensor": "2026-06-14 17:30:00"
}
```

Response:

- HTTP `201 Created`
- body `SensorReadingResponse`

### GET `/api/sensor-readings/latest/{microcontrollerId}`

Mengambil data terbaru untuk satu `microcontrollerId`.

Response:

- HTTP `200 OK`
- body `SensorReadingResponse`

### GET `/api/sensor-readings/history/{microcontrollerId}?limit=10`

Mengambil riwayat data sensor.

Response:

- HTTP `200 OK`
- body `List<SensorReadingResponse>`

## 10.2 `application-service`

Base URL:

```text
http://localhost:8082
```

### GET `/api/sensor-data/latest/{microcontrollerId}`

Meminta data terbaru dari `microcontroller-service`.

Response body:

```json
{
  "status": "success",
  "message": "Data sensor terbaru berhasil diambil",
  "microcontrollerId": "ESP32-001",
  "readings": []
}
```

### GET `/api/sensor-data/history/{microcontrollerId}?limit=10`

Meminta riwayat data sensor dari `microcontroller-service`.

Response body:

```json
{
  "status": "success",
  "message": "Riwayat data sensor berhasil diambil",
  "microcontrollerId": "ESP32-001",
  "readings": []
}
```

Catatan:

- `latest` mengembalikan 1 item di `readings`.
- `history` mengembalikan daftar data hingga batas `limit`.
- Jika data tidak ditemukan, service mengembalikan error dengan status not found.

## 11. Docker Compose Terbaru

`docker-compose.yml` saat ini menjalankan service berikut:

- `verdant-mysql`
- `rabbitmq`
- `microcontroller-service`
- `application-service`
- `prometheus`
- `grafana`

### 11.1 Port di Docker

- MySQL -> `3306`
- RabbitMQ AMQP -> `5672`
- RabbitMQ UI -> `15672`
- `microcontroller-service` -> `8081`
- `application-service` -> `8082`
- Prometheus -> `9090`
- Grafana -> `3000`

### 11.2 Network

Semua container menggunakan network:

- `verdant-network`

### 11.3 Environment variable di Docker

#### `microcontroller-service`

- `DB_HOST=verdant-mysql`
- `DB_PORT=3306`
- `DB_NAME_MICRO=microcontroller_service_db`
- `DB_USERNAME=root`
- `DB_PASSWORD=root`
- `RABBITMQ_HOST=rabbitmq`
- `RABBITMQ_PORT=5672`
- `RABBITMQ_USERNAME=user`
- `RABBITMQ_PASSWORD=password`

#### `application-service`

- `RABBITMQ_HOST=rabbitmq`
- `RABBITMQ_PORT=5672`
- `RABBITMQ_USERNAME=user`
- `RABBITMQ_PASSWORD=password`
- `MAIL_HOST=smtp.gmail.com`
- `MAIL_PORT=587`
- `MAIL_USERNAME=anasrudi048@gmail.com`
- `MAIL_PASSWORD=App Password Gmail`
- `MAIL_FROM=anasrudi048@gmail.com`

### 11.4 Catatan penting Docker

- Di dalam container, `localhost` tidak dipakai untuk koneksi antarservice.
- Gunakan nama service container seperti `verdant-mysql` dan `rabbitmq`.
- `docker-compose.yml` juga memasang healthcheck untuk MySQL dan RabbitMQ.

## 12. Monitoring

### 12.1 Prometheus

`prometheus.yml` saat ini scraping:

- `microcontroller-service:8081/actuator/prometheus`
- `application-service:8082/actuator/prometheus`

### 12.2 Grafana

Grafana tersedia di:

```text
http://localhost:3000
```

### 12.3 Actuator

Kedua service mengekspos metrics ke:

- `http://localhost:8081/actuator/health`
- `http://localhost:8081/actuator/info`
- `http://localhost:8081/actuator/prometheus`
- `http://localhost:8082/actuator/health`
- `http://localhost:8082/actuator/info`
- `http://localhost:8082/actuator/prometheus`

## 13. Catatan Testing

Jika ingin validasi cepat setelah menjalankan service:

1. POST data ke `microcontroller-service`.
2. GET `latest` dan `history` dari `microcontroller-service`.
3. GET `latest` dan `history` dari `application-service`.
4. Cek `actuator/health`.
5. Cek `actuator/prometheus`.

## 14. Ringkasan Singkat

Project ini terdiri dari `microcontroller-service` untuk menerima dan menyimpan data sensor ESP32, serta `application-service` untuk mengambil data sensor lewat RabbitMQ dan mengirim email otomatis saat data baru masuk. Database hanya dipakai oleh `microcontroller-service`, sementara observability sudah tersedia lewat Actuator, Prometheus, dan Grafana.
