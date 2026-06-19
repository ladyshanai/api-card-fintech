CREATE DATABASE IF NOT EXISTS `cards_db`
DEFAULT CHARACTER SET = 'utf8mb4'
COLLATE = 'utf8mb4_unicode_ci';

USE `cards_db`;

CREATE TABLE IF NOT EXISTS `cards` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `account_id` BIGINT NOT NULL,
                                       `card_number` VARCHAR(20) DEFAULT NULL,
    `card_type` VARCHAR(50) DEFAULT NULL,
    `card_status` VARCHAR(50) DEFAULT NULL,
    `card_brand` VARCHAR(50) DEFAULT NULL,
    `credit_limit` NUMERIC(15, 2) DEFAULT 0,
    `available_balance` NUMERIC(15, 2) DEFAULT 0,
    `used_balance` NUMERIC(15, 2) DEFAULT 0,
    `expiration_date` DATE NULL,
    `issue_date` DATE NULL,
    `created_at` DATETIME NULL,
    `updated_at` DATETIME NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_cards_account` (`account_id`),
    INDEX `idx_cards_status` (`card_status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `cards`
(`account_id`, `card_number`, `card_type`, `card_status`, `card_brand`,
 `credit_limit`, `available_balance`, `used_balance`,
 `expiration_date`, `issue_date`, `created_at`, `updated_at`)
VALUES
    (1, '**** **** **** 1234', 'CREDIT', 'ACTIVE', 'VISA',
     500000.00, 350000.00, 150000.00,
     DATE_ADD(CURDATE(), INTERVAL 3 YEAR), CURDATE(), NOW(), NOW()),

    (1, '**** **** **** 5678', 'DEBIT', 'ACTIVE', 'MASTERCARD',
     0.00, 50000.00, 0.00,
     DATE_ADD(CURDATE(), INTERVAL 4 YEAR), CURDATE(), NOW(), NOW()),

    (2, '**** **** **** 9012', 'CREDIT', 'BLOCKED', 'AMEX',
     750000.00, 600000.00, 150000.00,
     DATE_ADD(CURDATE(), INTERVAL 2 YEAR), CURDATE(), NOW(), NOW());