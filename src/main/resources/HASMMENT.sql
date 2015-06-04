-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 04-06-2015 a las 07:13:39
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
-- Estructura de tabla para la tabla `asiste`
--

CREATE TABLE IF NOT EXISTS `asiste` (
  `DNI_USU_ASI` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del usuario',
  `DNI_AUX_ASI` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del auxiliar',
  `FHORAINICIO_ASI` datetime NOT NULL COMMENT 'Fecha y hora de inicio de la asistencia',
  `FHORAFIN_ASI` datetime DEFAULT NULL COMMENT 'Fecha y hora de fin de la asistencia',
  `ACTIVIDAD_ASI` blob NOT NULL COMMENT 'Descripcion breve de la asistencia',
  PRIMARY KEY (`DNI_USU_ASI`,`DNI_AUX_ASI`,`FHORAINICIO_ASI`),
  KEY `FK_ASI_AUX` (`DNI_AUX_ASI`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auxiliar`
--

CREATE TABLE IF NOT EXISTS `auxiliar` (
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
-- Volcado de datos para la tabla `auxiliar`
--

INSERT INTO `auxiliar` (`DNI_AUX`, `NOMBRE_AUX`, `APELLIDO1_AUX`, `APELLIDO2_AUX`, `HORAS_AUX`, `FINICIO_AUX`, `FFIN_AUX`) VALUES
('22222222J', 'Jose', 'Lopez', 'Lopez', 10, '2015-05-28', '2015-06-07'),
('22222223Z', 'Luis', 'Martinez', 'Rodriguez', 20, '2015-05-28', '2015-06-12'),
('22222224S', 'Manuel', 'Castro', 'Ramirez', 30, '2015-06-06', '2015-06-25'),
('22222225Q', 'Paco', 'Gomez', 'Novoa', 40, '2015-06-27', '2015-08-22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permiso`
--

CREATE TABLE IF NOT EXISTS `permiso` (
  `DNI_AUX_PER` varchar(9) COLLATE latin1_spanish_ci NOT NULL COMMENT 'DNI del auxiliar',
  `FINICIO_PER` date NOT NULL COMMENT 'Fecha inicio del permiso',
  `FFIN_PER` date NOT NULL COMMENT 'Fecha fin del permiso',
  `TIPO_PER` varchar(20) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Vacaciones/Baja/Asuntos propios',
  PRIMARY KEY (`DNI_AUX_PER`,`FINICIO_PER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Permiso pedido por un auxiliar';

--
-- Volcado de datos para la tabla `permiso`
--

INSERT INTO `permiso` (`DNI_AUX_PER`, `FINICIO_PER`, `FFIN_PER`, `TIPO_PER`) VALUES
('22222222J', '2015-06-22', '2015-06-26', 'Vacaciones'),
('22222224S', '2015-06-22', '2015-06-23', 'Asuntos propios');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
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
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`DNI_USU`, `NOMBRE_USU`, `APELLIDO1_USU`, `APELLIDO2_USU`, `DIRECCION_USU`, `HORAS_USU`, `MODALIDAD_USU`) VALUES
('11111111H', 'Alvaro', 'Alvarez', 'Alvarez', 'Casa de Alvaro	', 10, 'Dependencia'),
('11111112L', 'Brais', 'Vazquez', 'Vazquez', 'Casa de Brais', 20, 'Dependencia'),
('11111113C', 'Carlos', 'Martinez', 'Martinez', 'Casa de Carlos', 30, 'Prestación Básica'),
('11111114K', 'David', 'Rodriguez', 'Rodriguez', 'Casa de David', 40, 'Prestación Básica');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asiste`
--
ALTER TABLE `asiste`
  ADD CONSTRAINT `FK_ASI_AUX` FOREIGN KEY (`DNI_AUX_ASI`) REFERENCES `auxiliar` (`DNI_AUX`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_ASI_USU` FOREIGN KEY (`DNI_USU_ASI`) REFERENCES `usuario` (`DNI_USU`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `permiso`
--
ALTER TABLE `permiso`
  ADD CONSTRAINT `FK_PER_AUX` FOREIGN KEY (`DNI_AUX_PER`) REFERENCES `auxiliar` (`DNI_AUX`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
