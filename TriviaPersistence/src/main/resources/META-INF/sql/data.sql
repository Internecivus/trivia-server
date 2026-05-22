INSERT INTO trivia_db.category
  (name, description, image, date_created) VALUES
  ('Computer Science', 'All about computers, programming languages and technology in general.', 'img_16003767386513246051.png', current_timestamp),
  ('History', 'Conquer the glorious events of the past!', 'img_1196442441472512309.png', current_timestamp);

INSERT INTO trivia_db.user
  (password, name, date_created, provider_key, provider_secret) VALUES
  ('9999:9RHkKIR6NU+Alv62U9C3w14tzfE=:qmcjFzuM1FTpbu7p+Te6WUkTArY=', 'admin', '2018-05-12', 'ZRTW4nVBGBfks6Df9LjRwBMFhrPMQw04', '9999:LNWwrcV1EPFloOpcbBz5t/5j1vk=:xhJZYq/z7IOd35RYUpVLhiSVM/8='), --4tojgeiJsiqUWGAOF9JwwtekZ1tzzfJk
  ('9999:RFKzY5goL/vDLAPuneqyZBRk+kA=:w0+mbBB8INJ+3FaraLl90j5iRT0=', 'trivia', '2018-05-12', 'yzJ4nRfas9/tCKH5bqWOYsfoE/+iHMAw', '9999:/ZfvSwsS3xAuAoBJMrm8XGo0ZO4=:d7dIR8AN0HcUtJakFfm7doPHQe0='); --87B2sDsxDtaboPvdExkPpuV77CmXJPGm

INSERT into trivia_db.client
  (api_key, api_secret, date_created, user_id) VALUES
  ('trivia', '9999:RFKzY5goL/vDLAPuneqyZBRk+kA=:w0+mbBB8INJ+3FaraLl90j5iRT0=', current_date, 1);

INSERT INTO trivia_db.role
  (name) VALUES
  ('CONTRIBUTOR'), ('PROVIDER'), ('MODERATOR'), ('ADMIN');

INSERT INTO trivia_db.user_role_map
  (user_id, role_id) VALUES
  (1, 1), (1, 2), (1, 3), (1, 4),
  (2, 1), (2, 2), (2, 3);

INSERT INTO trivia_db.question
  (question, answer_first, answer_second, answer_third, answer_fourth, answer_correct, user_id, comment, date_created, date_last_modified, image) VALUES
  ('What is the new name of Java EE under the stewardship of the Eclipse Foundation?', 'J3EE', 'Java EE', 'Jakarta EE', 'Java Eclipse',
    3, 1, null, current_timestamp, null, null),
  ('Which company made the first relational database?', 'IBM', 'Oracle', 'Microsoft', 'Google',
    1, 1, null, current_timestamp, null, null),
  ('Who most famously uttered the phrase "Nvidia, fuck you!" while giving the middle finger?', 'Richard Stallman', 'Linus Torvalds', 'Bill Gates', 'Steve Jobs',
    2, 1, null, current_timestamp, null, null),
  ('Which language is based on Mocha, a language made only in 10 days?', 'Java', 'Javascript', 'C#', 'C',
    2, 1, null, current_timestamp, null, null),
  ('Where did the computer term "bug" originate from?', 'The creature from the movie Alien', 'Virus-carrying mosquito', 'Swarm of locusts from the Bible', 'A moth found in an early computer',
    4, 1, null, current_timestamp, null, null),
  ('Which is the most used programming language (according to TIOBE)?', 'Java', 'C++', 'C#', 'Javascript',
    1, 1, null, current_timestamp, null, 'img_17683032077377249832.png'),
  ('What is considered to be the most expensive internet domain, valued at just under $50 million?', 'internet.com', 'carinsurance.com', 'sex.com', 'hotels.com',
    2, 1, null, current_timestamp, null, null),
  ('Who is widely considered to be the first computer programmer?', 'Donald Knuth', 'Bill Gates', 'Charles Babbage', 'Ada Lovelace',
    4, 1, null, current_timestamp, null, null),
  ('Which programming language was originally created just for the purpose of managing a personal website?', 'PHP', 'Javascript', 'Mocha', 'Go',
    1, 1, null, current_timestamp, null, null),
  ('Which name for a folder is reserved (i.e. you can''t create it yourself on a Windows OS)?', 'CON', 'SEX', 'ROOT', 'SYSTEM32',
    1, 1, null, current_timestamp, null, null),
  ('How many lines of code does the Linux kernel contain (including drivers)?', '100k', '1 million', '15 million', '50 million',
    3, 1, null, current_timestamp, null, 'img_17989407437740337254.png'),
  ('Which movie was denied an Academy Award nomination because it''s use of computer-generated graphics was deemed to be "cheating"?', 'Tron', 'Avatar', ' Alien', 'Jurassic Park',
    1, 1, null, current_timestamp, null, null),
  ('Which password did the U.S. use for it''s nuclear missiles controls for over eight years?', '0123456789', 'password', ' 00000000', 'strikerussia',
    3, 1, null, current_timestamp, null, 'img_11108316137734411686.png'),
  ('What does CAPTCHA stand for?', 'Can Anyone Precisely Tell Characters Herein Ascertained?', ' CAPture CHAracters', 'Completely Automated Public Turing test to tell Computers and Humans Apart', 'Complex Automation Provider for Training Computers How to Apply intelligence',
    3, 1, null, current_timestamp, null, null),
  ('What was the "dirty dozen" a name for?', 'First 12 computer viruses', 'Unused prototypes of a 1st edition iPhone', 'Developers who made the first IBM computer', '12 most wanted dark web websites',
    3, 1, null, current_timestamp, null, null),
  ('What was the original name for Windows?', 'Orange', 'Interface Manager', 'Automaton', 'Computer Screen 1.0',
    2, 1, null, current_timestamp, null, null),
  ('What was the first registered domain name?', 'www.symbolics.com', 'www.internet.com', 'www.com.com', 'wwww.domains.com',
    1, 1, null, current_timestamp, null, 'img_870650305982202907.png'),

    ('In which country was the Magna Carta written?', 'USA', 'England', 'France', 'Italy',
    2, 1, null, current_timestamp, null, null),
    ('Who did Caligula make consul?', 'A tomato plant', 'His son', 'A homeless man', 'A horse',
    4, 1, null, current_timestamp, null, null),
    ('How long did the shortest war in history last?', 'Half an hour', '1 day', '1 week', '1 month',
    1, 1, null, current_timestamp, null, null),
    ('What did the Romans use for mouthwash?', 'Animal blood', 'Honey', 'Vinegar', 'Human urine',
    4, 1, null, current_timestamp, null, null),
    ('Who was offered the presidency of Israel but declined?', 'Elvis Presley', 'Fidel Castro', 'Albert Einstein', 'Dwight D. Eisenhower',
    3, 1, null, current_timestamp, null, null),
    ('Which animal did Napoleon Bonaparte had to flee from after being attacked during a hunting trip?', 'Rabbits', 'Lions', 'Wild dogs', 'Foxes',
    1, 1, null, current_timestamp, null, null),
    ('Which of the following was an official court title in ancient Egypt?', 'The Royal Flatterer', 'Taster of Human Tears', 'The Angry Magician', 'Shepherd of the Royal Anus',
    4, 4, null, current_timestamp, null, null),
    ('What year did Columbus discover America?', '1492', '1502', '1222', '1678',
    1, 1, null, current_timestamp, null, null),
    ('Which historical person was responsible for killing the most amount of people (over 40 million)?', 'Adolf Hitler', 'Joseph Stalin', 'Genghis Khan', 'Alexander the Great',
    3, 1, null, current_timestamp, null, null),
    ('Who invented the arabic numerals?', 'The Arabs', 'The Greeks', 'The Indians', 'The French',
    3, 1, null, current_timestamp, null, null),
    ('Why did Charles VI of France not want to be touched?', 'He had OCD', 'He thought he was made of glass', 'He believed nobody was worthy', 'He had very tender skin',
    2, 1, null, current_timestamp, null, null),
    ('What did most soldiers during the US Civil War die from?', 'Battle-related wounds', 'Diseases such as diarrhea', 'Suicide', 'Incompetent doctors',
    2, 1, null, current_timestamp, null, null),
    ('What did Julius Caesar tell the pirates who kidnapped him and demanded 20 gold pieces for his release?', 'All the answers are correct', 'That he is going to kill them after they release him', 'That he is worth at least 50', 'He made friends with them',
    1, 1, null, current_timestamp, null, null),
    ('In 2018, the USA has existed for 242 years. How many of these has it been at war?', '25', '102', '222', '241',
    4, 1, null, current_timestamp, null, null),
    ('Who killed JFK?', 'J. Edgar Hoover', 'Jack Ruby', 'Lee Harvey Oswald', 'Lyndon B. Johnson',
    3, 1, null, current_timestamp, null, null);

INSERT INTO trivia_db.question_category_map
  (question_id, category_id) VALUES
  (1, 1),(2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1), (11, 1), (12, 1), (13, 1),
  (14, 1), (15, 1), (16, 1), (17, 1),
  (18, 2), (19, 2), (20, 2), (21, 2), (22, 2), (23, 2), (24, 2), (25, 2), (26, 2), (27, 2), (28, 2), (29, 2), (30, 2),
  (32, 2), (33, 2);









