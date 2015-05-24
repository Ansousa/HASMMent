-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 24-05-2015 a las 15:55:46
-- Versión del servidor: 5.5.43-0ubuntu0.14.04.1
-- Versión de PHP: 5.5.9-1ubuntu4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `HASMMENT`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Asiste`
--

CREATE TABLE IF NOT EXISTS `Asiste` (
  `DNI_USU_ASI` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del usuario',
  `DNI_AUX_ASI` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del auxiliar',
  `FHORAINICIO_ASI` datetime DEFAULT NULL COMMENT 'Fecha y hora de inicio de la asistencia',
  `FHORAFIN_ASI` datetime DEFAULT NULL COMMENT 'Fecha y hora de fin de la asistencia',
  `ACTIVIDAD_ASI` varchar(200) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Descripcion breve de la asistencia',
  PRIMARY KEY (`DNI_USU_ASI`,`DNI_AUX_ASI`),
  KEY `FK_ASI_AUX` (`DNI_AUX_ASI`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `Asiste`
--

INSERT INTO `Asiste` (`DNI_USU_ASI`, `DNI_AUX_ASI`, `FHORAINICIO_ASI`, `FHORAFIN_ASI`, `ACTIVIDAD_ASI`) VALUES
('11111111H', '22222222J', '2015-05-19 00:00:00', '2015-05-21 00:00:00', 'Limpiar');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Auxiliar`
--

CREATE TABLE IF NOT EXISTS `Auxiliar` (
  `DNI_AUX` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del auxiliar',
  `NOMBRE_AUX` varchar(50) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del auxiliar',
  `APELLIDO1_AUX` varchar(30) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Primer apellido del auxiliar',
  `APELLIDO2_AUX` varchar(30) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Segundo apellido del auxiliar',
  `HORAS_AUX` int(11) NOT NULL COMMENT 'Horas contratadas',
  `FINICIO_AUX` date NOT NULL COMMENT 'Fecha de inicio de contratacion',
  `FFIN_AUX` date DEFAULT NULL COMMENT 'Fecha fin de contratacion',
  PRIMARY KEY (`DNI_AUX`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `Auxiliar`
--

INSERT INTO `Auxiliar` (`DNI_AUX`, `NOMBRE_AUX`, `APELLIDO1_AUX`, `APELLIDO2_AUX`, `HORAS_AUX`, `FINICIO_AUX`, `FFIN_AUX`) VALUES
('22222222J', 'Auxiliar1', 'Auxiliarez1', '', 40, '2015-05-18', '2015-05-18'),
('44496465K', 'Adrian', 'Novoa', 'Sousa', 21, '2015-05-18', '2015-05-18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Permiso`
--

CREATE TABLE IF NOT EXISTS `Permiso` (
  `DNI_AUX_PER` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del auxiliar',
  `FINICIO_PER` date NOT NULL COMMENT 'Fecha inicio del permiso',
  `FFIN_PER` date NOT NULL COMMENT 'Fecha fin del permiso',
  `TIPO_PER` varchar(20) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Vacaciones/Baja/Asuntos propios',
  PRIMARY KEY (`DNI_AUX_PER`,`FINICIO_PER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Permiso pedido por un auxiliar';

--
-- Volcado de datos para la tabla `Permiso`
--

INSERT INTO `Permiso` (`DNI_AUX_PER`, `FINICIO_PER`, `FFIN_PER`, `TIPO_PER`) VALUES
('22222222J', '2015-05-19', '2015-05-22', 'Vacaciones');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuario`
--

CREATE TABLE IF NOT EXISTS `Usuario` (
  `DNI_USU` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del usuario',
  `NOMBRE_USU` varchar(50) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del usuario',
  `APELLIDO1_USU` varchar(30) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Primer apellido del usuario',
  `APELLIDO2_USU` varchar(30) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Segundo apellido del usuario',
  `DIRECCION_USU` varchar(200) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Direccion del usuario',
  `HORAS_USU` int(11) NOT NULL COMMENT 'Horas al mes',
  `MODALIDAD_USU` varchar(20) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Dependencia / Prestacion basica',
  PRIMARY KEY (`DNI_USU`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Usuario de la aplicacion (solicitante de la prestacion)';

--
-- Volcado de datos para la tabla `Usuario`
--

INSERT INTO `Usuario` (`DNI_USU`, `NOMBRE_USU`, `APELLIDO1_USU`, `APELLIDO2_USU`, `DIRECCION_USU`, `HORAS_USU`, `MODALIDAD_USU`) VALUES
('11111111H', 'Usuario1', 'Usuariez1', 'Usuariez1', 'Sitio del Usuario1', 40, 'Prestación Básica');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Asiste`
--
ALTER TABLE `Asiste`
  ADD CONSTRAINT `FK_ASI_AUX` FOREIGN KEY (`DNI_AUX_ASI`) REFERENCES `Auxiliar` (`DNI_AUX`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_ASI_USU` FOREIGN KEY (`DNI_USU_ASI`) REFERENCES `Usuario` (`DNI_USU`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Permiso`
--
ALTER TABLE `Permiso`
  ADD CONSTRAINT `FK_PER_AUX` FOREIGN KEY (`DNI_AUX_PER`) REFERENCES `Auxiliar` (`DNI_AUX`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;