DROP TABLE IF EXISTS NOTES;
DROP TABLE IF EXISTS FILES;
DROP TABLE IF EXISTS CREDENTIALS;
DROP TABLE IF EXISTS USERS1;

CREATE TABLE IF NOT EXISTS USERS1 (
  userid INT PRIMARY KEY auto_increment,
  username VARCHAR(20) UNIQUE,
  salt VARCHAR(100),
  password VARCHAR(400), -- At the time of persist hashedPassword takes up maximum 344 chars 
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY auto_increment,
    notetitle VARCHAR(50),
    notedescription VARCHAR (2000),
    userid INT,
    foreign key (userid) references USERS1(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileId INT PRIMARY KEY auto_increment,
    filename VARCHAR(255),
    contenttype VARCHAR(255),
    filesize VARCHAR(255),
    userid INT,
    filedata MEDIUMBLOB, -- When uing MySQL, BLOB accepts upto 65.5 KB, larger than that results in 
                         -- com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: 
                         -- Data too long for column 'filedata' at row 1
    foreign key (userid) references USERS1(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid INT PRIMARY KEY auto_increment,
    url VARCHAR(100),
    username VARCHAR(30),
    keyenc  VARCHAR(255),             
    encodedpassword VARCHAR(100), -- At the time of persist encodedpassword takes up maximum 44 chars
    userid INT,
    foreign key (userid) references USERS1(userid)
);
