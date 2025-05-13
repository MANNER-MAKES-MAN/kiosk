-- 1) 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS kiosk
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE kiosk;
SET NAMES utf8mb4;

-- 2) 기존 객체 삭제
DROP TRIGGER IF EXISTS MenuAdd;
DROP TABLE    IF EXISTS VoiceRecognitionLog;
DROP TABLE    IF EXISTS NFC_Tag;
DROP TABLE    IF EXISTS OrderHistory;
DROP TABLE    IF EXISTS Menu;
DROP TABLE    IF EXISTS MenuSeq;
DROP TABLE    IF EXISTS AudioSeq;

-- 3) 테이블 생성
CREATE TABLE Menu (
  MenuID     VARCHAR(15) PRIMARY KEY,
  Name       VARCHAR(255) NOT NULL,
  Price      INT          NOT NULL,
  Stock      INT    DEFAULT 0,
  ImagePath  VARCHAR(255),
  AudioGuide VARCHAR(255),
  Category   VARCHAR(50)
);

CREATE TABLE OrderHistory (
  OrderID     VARCHAR(20) PRIMARY KEY,
  OrderTime   DATETIME    DEFAULT CURRENT_TIMESTAMP,
  MenuID      VARCHAR(12),
  MenuName    VARCHAR(100),
  Price       INT,
  Quantity    INT,
  TotalPrice  INT,
  OrderMethod VARCHAR(50),
  FOREIGN KEY (MenuID) REFERENCES Menu(MenuID)
);

CREATE TABLE NFC_Tag (
  TagID        VARCHAR(100) PRIMARY KEY,
  MappedMenuID VARCHAR(12),
  UseCount     INT         DEFAULT 0,
  FOREIGN KEY (MappedMenuID) REFERENCES Menu(MenuID)
);

CREATE TABLE VoiceRecognitionLog (
  VoiceLogID    VARCHAR(12) PRIMARY KEY,
  SpokenText    TEXT         NOT NULL,
  MatchedMenuID VARCHAR(12),
  TimeLogged    DATETIME     DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (MatchedMenuID) REFERENCES Menu(MenuID)
);

CREATE TABLE MenuSeq (
  seq INT AUTO_INCREMENT PRIMARY KEY
);
CREATE TABLE AudioSeq (
  id  INT AUTO_INCREMENT PRIMARY KEY
);

-- 4) 트리거 정의
DELIMITER //
CREATE TRIGGER MenuAdd
BEFORE INSERT ON Menu
FOR EACH ROW
BEGIN
  SET NEW.MenuID = CONCAT(
    '000-',
    LPAD(
      (SELECT IFNULL(MAX(CAST(SUBSTRING(MenuID,5) AS UNSIGNED)),0)+1 FROM Menu),
      9,'0'
    )
  );
  IF NEW.Stock IS NULL THEN
    SET NEW.Stock = 0;
  END IF;
  INSERT INTO AudioSeq VALUES (NULL);
  SET @audioSeq = LAST_INSERT_ID();
  SET NEW.AudioGuide = CONCAT('menu', LPAD(@audioSeq,9,'0'),'.wav');
END;
//
DELIMITER ;

-- 5) 테스트 데이터
INSERT INTO Menu (Name, Price, Category)
  VALUES ('아메리카노', 2000, '커피');
