-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 14 Jun 2026 pada 16.17
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `microcontroller_service_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `sensor_readings`
--

CREATE TABLE `sensor_readings` (
  `id` bigint(20) NOT NULL,
  `action` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `microcontroller_id` varchar(100) NOT NULL,
  `moisture_percent` varchar(255) NOT NULL,
  `pump_duration` varchar(255) NOT NULL,
  `sensor_value` varchar(255) NOT NULL,
  `soil_condition` varchar(255) NOT NULL,
  `timestamp_sensor` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `sensor_readings`
--

INSERT INTO `sensor_readings` (`id`, `action`, `created_at`, `microcontroller_id`, `moisture_percent`, `pump_duration`, `sensor_value`, `soil_condition`, `timestamp_sensor`, `email`) VALUES
(8, 'Pump ON', '2026-06-14 18:41:18.000000', 'ESP32-001', '68', '5 seconds', '742', 'Normal', '2026-06-14 17:30:00', 'decalyps@gmail.com'),
(9, 'Pump ON', '2026-06-14 18:41:19.000000', 'ESP32-001', '68', '5 seconds', '742', 'Normal', '2026-06-14 17:30:00', 'decalyps@gmail.com'),
(10, 'Pump ON', '2026-06-14 18:41:20.000000', 'ESP32-001', '68', '5 seconds', '742', 'Normal', '2026-06-14 17:30:00', 'decalyps@gmail.com'),
(11, 'Pump ON', '2026-06-14 20:08:52.000000', 'ESP32-001', '68', '5 seconds', '742', 'Normal', '2026-06-14 17:30:00', 'decalyps@gmail.com'),
(12, 'Pump ON', '2026-06-14 20:08:54.000000', 'ESP32-001', '68', '5 seconds', '742', 'Normal', '2026-06-14 17:30:00', 'decalyps@gmail.com'),
(13, 'Pump ON', '2026-06-14 20:08:54.000000', 'ESP32-001', '68', '5 seconds', '742', 'Normal', '2026-06-14 17:30:00', 'decalyps@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `sensor_readings`
--
ALTER TABLE `sensor_readings`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `sensor_readings`
--
ALTER TABLE `sensor_readings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
