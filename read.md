#程序执行
`java -jar ***.jar C:\**\**\**.json`
#json样例
`{
       "model": 6,      
       "date": [
         {
           "schoolId": "15",
           "gradeId": "25",
           "classId": "206"
         },
         {
           "schoolId": "15",
           "gradeId": "25",
           "classId": "168"
         },
         {
           "schoolId": "15",
           "gradeId": "22",
           "classId": "120"
         },
         {
           "schoolId": "15",
           "gradeId": "22",
           "classId": "121"
         },
         {
           "schoolId": "15",
           "gradeId": "22",
           "classId": "122"
         },
         {
           "schoolId": "15",
           "gradeId": "22",
           "classId": "123"
         },
         {
           "schoolId": "15",
           "gradeId": "23",
           "classId": "124"
         },
         {
           "schoolId": "15",
           "gradeId": "23",
           "classId": "125"
         },
         {
           "schoolId": "15",
           "gradeId": "23",
           "classId": "126"
         }
       ]
     }`
  ##说明
  ###json 文件
  
   * model=1 只计算个人问题行为成绩
   * model=2 只计算个人积极心理品质成绩
   * model=3 计算个人问题行为和积极心理品质成绩
   * model=4 只计算班级问题行为成绩
   * model=5 只计算班级积极心理品质成绩
   * model=6 计算班级问题行为和积极心理品质成绩
   ---
   date中为一个数组样式，数组中包含多个{}，每个{}中放置一个要计算的班级，{}中信息有schoolId，gradeId，classId信息，仿照文中或代码中db/example.json即可。
   
   
   