
# MySQL DB

CREATE DATABASE FPLANNER;
USE FPLANNER;

#SET @delim_length=5;
#SET @name_length=20;
#SET @pswd_length=32;
#CREATE USER fplanuser identified by 'f80e57c44bae595c2509a2fd659cda49';
CREATE USER fplanuser identified by 'SomePass';
#grant all privileges on *.* to fplanuser with grant option;
grant all privileges on *.* to fplanuser;


CREATE TABLE TB_USERS (
    COL_UNAME          VARCHAR(20)    NOT NULL PRIMARY KEY,
    COL_U_PSWD         VARCHAR(32)    NOT NULL,
    COL_U_ROOT_PSWD    VARCHAR(32)    NOT NULL,
    COL_U_EMAIL        VARCHAR(50)    NOT NULL,
    COL_U_GECOS        VARCHAR(255)       NULL
);


CREATE TABLE TB_MEMBERS (
    COL_MNAME   VARCHAR(45)     NOT NULL    PRIMARY KEY,
    COL_UNAME   VARCHAR(20)     NOT NULL,
    COL_M_EMAIL VARCHAR(50)         NULL,
    COL_M_GECOS VARCHAR(255)        NULL,
    COL_M_PSWD  VARCHAR(32)         NULL
);


CREATE TABLE TB_WALLET (
    COL_WNAME         VARCHAR(70)    NOT NULL    PRIMARY KEY,
    COL_MNAME         VARCHAR(45)    NOT NULL
);


CREATE TABLE TB_FLOW (
    COL_WNAME       VARCHAR(70)    NOT NULL,
    COL_MNAME       VARCHAR(45)    NOT NULL,
    COL_PLNAME      VARCHAR(64)        NULL,
    COL_CATNAME     VARCHAR(32)        NULL,
    COL_FL_DATE     NUMERIC(14,0)  NOT NULL,
    COL_FL_AMOUNT   NUMERIC(15,2)  NOT NULL,
    COL_FL_NEGATIVE NUMERIC(1,0)   NOT NULL,
    COL_FL_COMMENT  VARCHAR(255)   NOT NULL

);


CREATE TABLE TB_PLAN (
    COL_PLNAME      VARCHAR(64)     NOT NULL    PRIMARY KEY,
    COL_PL_TYPE     NUMERIC(5)      NOT NULL,
    COL_PL_COMMENT  VARCHAR(255)    NOT NULL,
    COL_WNAME       VARCHAR(70)     NOT NULL,
    COL_PL_START    NUMERIC(14, 0)  NOT NULL,
    COL_PL_END      NUMERIC(14, 0)  NOT NULL,
    COL_PL_EST_OUT  NUMERIC(15,2)       NULL,
    COL_PL_EST_IN   NUMERIC(15,2)       NULL,
    COL_PL_EST_BAL  NUMERIC(15,2)       NULL 
);


CREATE TABLE TB_CATEGORY (
    COL_CATNAME    VARCHAR(70)  NOT NULL,
    COL_MNAME      VARCHAR(45)  NOT NULL
);

