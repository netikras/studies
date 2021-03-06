# CREATE DATABASE planner;

CREATE TABLE TB_USERS (
	COL_U_NAME 			VARCHAR2(20) 	NOT NULL PRIMARY KEY,
	COL_U_PSWD 			VARCHAR2(32) 	NOT NULL,
	COL_U_EMAIL 		VARCHAR2(50) 	NOT NULL,
	COL_U_GECKOS 		VARCHAR2(255) 	    NULL,
	COL_U_MEMBERS_CNT 	NUMBER(20,0) 	NOT NULL
);


CREATE TABLE TB_MEMBERS (
	COL_M_ID		VARCHAR2(52)	NOT NULL	PRIMARY KEY,
	COL_M_NAME		VARCHAR2(50)	NOT NULL,
	COL_M_EMAIL		VARCHAR2(50)	    NULL,
	COL_M_PSWD		VARCHAR2(32)	    NULL,
	COL_M_UNAME		VARCHAR2(20)	NOT NULL
);


CREATE TABLE TB_FLOW (
	COL_FL_ID		VARCHAR2(64)	NOT NULL	PRIMARY KEY,
	COL_FL_DATE		NUMBER(14,0)	NOT NULL,
	COL_FL_AMOUNT	NUMBER(2,20)	NOT NULL,
	COL_FL_CATID	VARCHAR2(32)	NOT NULL,
	COL_FL_WID		VARCHAR2(84)	    NULL
);


CREATE TABLE TB_INFLOW (
	COL_IFL_ID				VARCHAR2(102)	NOT NULL,
	COL_IFL_AMOUNT			NUMBER(2,20)	NOT NULL,
	COL_IFL_WID				VARCHAR2(84)	NOT NULL,
	COL_IFL_CAT_ID			VARCHAR2(20)	NOT NULL,
	COL_IFL_COMMENT			VARCHAR2(256)	NOT NULL,
	COL_IFL_SRC_BANK_ACC	VARCHAR2(20)	    NULL
);




CREATE TABLE TB_OUTFLOW (
	COL_OFL_ID				VARCHAR2(102)	NOT NULL,
	COL_OFL_AMOUNT			NUMBER(2,20)	NOT NULL,
	COL_OFL_WID				VARCHAR2(84)	NOT NULL,
	COL_OFL_CAT_ID			VARCHAR2(20)	NOT NULL,
	COL_OFL_COMMENT			VARCHAR2(256)	NOT NULL,
	COL_OFL_DST_BANK_ACC	VARCHAR2(20)	    NULL
);


CREATE TABLE TB_PLAN (
	COL_PL_ID		VARCHAR2(64)	NOT NULL	PRIMARY KEY,
	COL_PL_TITLE	VARCHAR2(50)	NOT NULL,
	COL_PL_WID		VARCHAR2(84)	NOT NULL,
	COL_PL_COMMENT	VARCHAR2(50)	NOT NULL
);



CREATE TABLE TB_WALLET (
	COL_W_ID			VARCHAR2(84)	NOT NULL	PRIMARY KEY,
	COL_W_NAME			VARCHAR2(50)	NOT NULL,
	COL_W_BANK_ACC		VARCHAR2(50)	NOT NULL,
	COL_W_VISIBILITY	NUMBER(5,0)		NOT NULL,
	COL_W_AMOUNT		NUMBER(2,20)	NOT NULL,
	COL_W_MID			VARCHAR2(52)	NOT NULL
);


CREATE TABLE TB_CATEGORY (
	COL_CAT_ID		VARCHAR2(32)	NOT NULL	PRIMARY KEY,
	COL_CAT_TITLE	VARCHAR2(50)	NOT NULL,
	COL_CAT_MID		VARCHAR2(32)	NOT NULL
);






############# MYSQL ##############



CREATE DATABASE FPLANNER;
USE FPLANNER;

CREATE TABLE TB_USERS (
    COL_U_NAME         VARCHAR(20)    NOT NULL PRIMARY KEY,
    COL_U_PSWD         VARCHAR(32)    NOT NULL,
    COL_U_EMAIL        VARCHAR(50)    NOT NULL,
    COL_U_GECKOS       VARCHAR(255)       NULL,
    COL_U_MEMBERS_CNT  NUMERIC(20,0)  NOT NULL
);


CREATE TABLE TB_MEMBERS (
    COL_M_ID    VARCHAR(52) NOT NULL    PRIMARY KEY,
    COL_M_NAME  VARCHAR(50) NOT NULL,
    COL_M_EMAIL VARCHAR(50)     NULL,
    COL_M_PSWD  VARCHAR(32)	    NULL,
    COL_M_UNAME VARCHAR(20)	NOT NULL
);


CREATE TABLE TB_FLOW (
    COL_FL_ID     VARCHAR(64)    NOT NULL    PRIMARY KEY,
    COL_FL_DATE   NUMERIC(14,0)  NOT NULL,
    COL_FL_AMOUNT NUMERIC(20,2)  NOT NULL,
    COL_FL_CATID  VARCHAR(32)    NOT NULL,
    COL_FL_WID    VARCHAR(84)        NULL
);


CREATE TABLE TB_INFLOW (
    COL_IFL_ID            VARCHAR(102)   NOT NULL,
    COL_IFL_AMOUNT        NUMERIC(20,2)  NOT NULL,
    COL_IFL_WID           VARCHAR(84)    NOT NULL,
    COL_IFL_CAT_ID        VARCHAR(20)    NOT NULL,
    COL_IFL_COMMENT       VARCHAR(256)   NOT NULL,
    COL_IFL_SRC_BANK_ACC  VARCHAR(20)        NULL
);




CREATE TABLE TB_OUTFLOW (
    COL_OFL_ID            VARCHAR(102)   NOT NULL,
    COL_OFL_AMOUNT        NUMERIC(20,2)  NOT NULL,
    COL_OFL_WID           VARCHAR(84)    NOT NULL,
    COL_OFL_CAT_ID        VARCHAR(20)    NOT NULL,
    COL_OFL_COMMENT       VARCHAR(256)   NOT NULL,
    COL_OFL_DST_BANK_ACC  VARCHAR(20)        NULL
);


CREATE TABLE TB_PLAN (
    COL_PL_ID       VARCHAR(64) NOT NULL    PRIMARY KEY,
    COL_PL_TITLE    VARCHAR(50) NOT NULL,
    COL_PL_WID      VARCHAR(84) NOT NULL,
    COL_PL_COMMENT  VARCHAR(50) NOT NULL
);



CREATE TABLE TB_WALLET (
    COL_W_ID          VARCHAR(84)    NOT NULL    PRIMARY KEY,
    COL_W_NAME        VARCHAR(50)    NOT NULL,
    COL_W_BANK_ACC    VARCHAR(50)    NOT NULL,
    COL_W_VISIBILITY  NUMERIC(5,0)   NOT NULL,
    COL_W_AMOUNT      NUMERIC(20,2)  NOT NULL,
    COL_W_MID         VARCHAR(52)    NOT NULL
);


CREATE TABLE TB_CATEGORY (
    COL_CAT_ID     VARCHAR(32)  NOT NULL    PRIMARY KEY,
    COL_CAT_TITLE  VARCHAR(50)  NOT NULL,
    COL_CAT_MID    VARCHAR(32)  NOT NULL
);
