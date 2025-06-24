INSERT INTO H2_SCHOOL.STUDENTS_BAK ( SID , FULL_NAME , BIRTHDAY , LEVEL)
 SELECT [SID] ,  FULL_NAME , BIRTHDAY , LEVEL
  FROM H2_SCHOOL.STUDENTS
   WHERE SID = {SID};
-- [] is optional parameter(s) & {} is required parameter(s)
-- [] can be null as ...,AGE,null