# Verdant Flow Service Server

Backend microservices Spring Boot untuk monitoring sensor ESP32, dengan MySQL, RabbitMQ, email notifikasi, serta monitoring Prometheus dan Grafana.

## Ringkasan

- `microcontroller-service` menerima data sensor, menyimpannya ke MySQL, lalu mem-publish event notifikasi ke RabbitMQ.
- `application-service` mengambil data sensor dari `microcontroller-service` saat aplikasi meminta data.
- `application-service` juga menerima event notifikasi untuk mengirim email ke user.
- `application-service` tidak memakai database sendiri.
- `docker-compose.yml` terbaru juga menjalankan `prometheus` dan `grafana`.

Dokumentasi teknis lengkap ada di [`PROJECT-DOCUMENTATION.md`](./PROJECT-DOCUMENTATION.md).

## Alur Singkat

1. ESP32 mengirim data ke `microcontroller-service`.
2. `microcontroller-service` menyimpan data ke database MySQL.
3. Setelah data tersimpan, service ini mengirim event ke RabbitMQ.
4. `application-service` menerima event tersebut dan mengirim email.
5. Saat aplikasi meminta data, `application-service` mengirim request ke RabbitMQ dan meneruskan hasilnya ke aplikasi.

## Komponen Utama

- `microcontroller-service/`
- `application-service/`
- `init/microcontroller_service_db.sql`
- `docker-compose.yml`
- `prometheus.yml`

## Jalankan Lokal

Jalankan masing-masing service dari foldernya:

```powershell
cd microcontroller-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd application-service
.\mvnw.cmd spring-boot:run
```

## Testing Cepat

### microcontroller-service

```http
POST http://localhost:8081/api/sensor-readings
GET http://localhost:8081/api/sensor-readings/latest/ESP32-001
GET http://localhost:8081/api/sensor-readings/history/ESP32-001?limit=10
```

### application-service

```http
GET http://localhost:8082/api/sensor-data/latest/ESP32-001
GET http://localhost:8082/api/sensor-data/history/ESP32-001?limit=10
```

## Jalankan dengan Docker

```powershell
docker compose up -d --build
```

### Port yang Dipakai

- `microcontroller-service` -> `http://localhost:8081`
- `application-service` -> `http://localhost:8082`
- RabbitMQ UI -> `http://localhost:15672`
- RabbitMQ AMQP -> `5672`
- MySQL -> `3306`
- Prometheus -> `http://localhost:9090`
- Grafana -> `http://localhost:3000`

## Monitoring

- Setiap service mengekspos `actuator/health`, `actuator/info`, dan `actuator/prometheus`.
- `prometheus.yml` sudah diarahkan ke:
  - `microcontroller-service:8081/actuator/prometheus`
  - `application-service:8082/actuator/prometheus`

## Catatan

- `application-service` tidak memiliki database sendiri.
- `MAIL_PASSWORD` harus diisi dengan Gmail App Password.
- `init/microcontroller_service_db.sql` dipakai untuk inisialisasi tabel `sensor_readings`.
