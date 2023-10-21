-- ----------------------------------------------------------------------------
-- MySQL Workbench Migration
-- Migrated Schemata: GiftCertificates
-- Source Schemata: GiftCertificates
-- Created: Mon Jul 31 13:19:39 2023
-- Workbench Version: 8.0.33
-- ----------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------------
-- Schema GiftCertificates
-- ----------------------------------------------------------------------------
DROP SCHEMA IF EXISTS `GiftCertificates` ;
CREATE SCHEMA IF NOT EXISTS `GiftCertificates` ;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.certificates
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`certificates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(32) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `duration` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 126
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.certificates_to_tags
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`certificates_to_tags` (
  `certificate_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  UNIQUE INDEX `Duplicate_constrint` (`certificate_id` ASC, `tag_id` ASC) VISIBLE,
  INDEX `tag_foreign_key` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `certificates_foreignkey_constraint`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `GiftCertificates`.`certificates` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `tags_foreignkey_constraint`
    FOREIGN KEY (`tag_id`)
    REFERENCES `GiftCertificates`.`tags` (`tag_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.orders
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `order_price` DECIMAL(10,2) NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `GiftCertificates`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 98
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.orders_certificates
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`orders_certificates` (
  `order_id` INT NOT NULL,
  `certificate_id` INT NOT NULL,
  INDEX `order_key_idx` (`order_id` ASC) VISIBLE,
  INDEX `cert_key_idx` (`certificate_id` ASC) VISIBLE,
  CONSTRAINT `cert_key`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `GiftCertificates`.`certificates` (`id`),
  CONSTRAINT `order_key`
    FOREIGN KEY (`order_id`)
    REFERENCES `GiftCertificates`.`orders` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.tags
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`tags` (
  `tag_id` INT NOT NULL AUTO_INCREMENT,
  `tag_name` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE INDEX `tag_name_UNIQUE` (`tag_name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 59
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.user_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`user_role` (
  `user_id` INT NOT NULL,
  `roles` VARCHAR(255) NULL DEFAULT NULL,
  INDEX `FKj345gk1bovqvfame88rcx7yyx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx`
    FOREIGN KEY (`user_id`)
    REFERENCES `GiftCertificates`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table GiftCertificates.users
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftCertificates`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
SET FOREIGN_KEY_CHECKS = 1;
