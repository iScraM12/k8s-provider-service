-- This script is executed on the first startup of the MariaDB container.
-- Using backticks for table and column names to avoid potential keyword conflicts.

CREATE TABLE IF NOT EXISTS `Car` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `brand` VARCHAR(255),
    `model` VARCHAR(255),
    `color` VARCHAR(255),
    PRIMARY KEY (`id`)
);

-- Insert some sample data (optional)
INSERT INTO `Car` (`brand`, `model`, `color`) VALUES ('Volkswagen', 'Golf', 'Blue');
INSERT INTO `Car` (`brand`, `model`, `color`) VALUES ('Audi', 'A4', 'Black');
INSERT INTO `Car` (`brand`, `model`, `color`) VALUES ('Mercedes-Benz', 'C-Class', 'Silver');
