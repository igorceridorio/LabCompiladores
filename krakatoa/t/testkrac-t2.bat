REM Segundo trabalho. 
REM testa a geração de código 

del ok-*.txt
del er-*.txt
del *.c
del z.txt
del r.txt
del ok-*.exe
del er-*.exe

rem del ..\..\t\ok-*.txt
rem del ..\..\t\er-*.txt
rem del ..\..\t\ok-*.exe
rem del ..\..\t\er-*.exe
rem del ..\..\..\tests\ok-*.txt
rem del ..\..\..\tests\er-*.txtcd
rem del ..\..\..\tests\ok-*.exe
rem del ..\..\..\tests\er-*.exe
rem del ..\..\..\tests\ok-*.c



java -cp . comp.Comp ..\..\..\tests\OK-GER01.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER02.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER03.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER04.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER05.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER06.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER07.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER08.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER09.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER10.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER11.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER12.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER14.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER15.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER16.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER17.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER18.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER19.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER20.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER21.KRA
java -cp . comp.Comp ..\..\..\tests\OK-GER22.KRA

pause

rem move ..\..\t\ok-*.txt .
rem move ..\..\t\er-*.txt .
rem move ..\..\t\ok-*.exe .
rem move ..\..\..\tests\ok-*.txt .
rem move ..\..\..\tests\er-*.txt .
rem move ..\..\..\tests\ok-*.c .
rem move ..\..\..\tests\ok-*.exe .



set path="C:\ProgFiles\MinGW\bin";%path%

del z.txt

gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER01.exe OK-GER01.kra.c
OK-GER01  < ..\..\..\t\30-enters.txt > OK-Out01.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER02.exe OK-GER02.kra.c
OK-GER02  < ..\..\..\t\30-enters.txt > OK-Out02.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER03.exe OK-GER03.kra.c
OK-GER03  < ..\..\..\t\30-enters.txt > OK-Out03.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER04.exe OK-GER04.kra.c
OK-GER04  < ..\..\..\t\30-enters.txt > OK-Out04.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER06.exe OK-GER06.kra.c
OK-GER06  < ..\..\..\t\30-enters.txt > OK-Out06.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER07.exe OK-GER07.kra.c
OK-GER07  < ..\..\..\t\30-enters.txt > OK-Out07.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER08.exe OK-GER08.kra.c
OK-GER08  < ..\..\..\t\30-enters.txt > OK-Out08.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER09.exe OK-GER09.kra.c
OK-GER09  < ..\..\..\t\30-enters.txt > OK-Out09.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER10.exe OK-GER10.kra.c
OK-GER10  < ..\..\..\t\30-enters.txt > OK-Out10.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER12.exe OK-GER12.kra.c
OK-GER12  < ..\..\..\t\30-enters.txt > OK-Out12.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER14.exe OK-GER14.kra.c
OK-GER14  < ..\..\..\t\30-enters.txt > OK-Out14.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER15.exe OK-GER15.kra.c
OK-GER15  < ..\..\..\t\30-enters.txt > OK-Out15.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER16.exe OK-GER16.kra.c
OK-GER16  < ..\..\..\t\30-enters.txt > OK-Out16.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER17.exe OK-GER17.kra.c
OK-GER17  < ..\..\..\t\30-enters.txt > OK-Out17.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER18.exe OK-GER18.kra.c
OK-GER18  < ..\..\..\t\30-enters.txt > OK-Out18.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER19.exe OK-GER19.kra.c
OK-GER19  < ..\..\..\t\30-enters.txt > OK-Out19.txt

gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER20.exe OK-GER20.kra.c
OK-GER20  < ..\..\..\t\30-enters.txt > OK-Out20.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER21.exe OK-GER21.kra.c
OK-GER21  < ..\..\..\t\30-enters.txt > OK-Out21.txt
gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER22.exe OK-GER22.kra.c
OK-GER22  < ..\..\..\t\30-enters.txt > OK-Out22.txt


copy OK-Out*.txt z.txt

gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER05.exe OK-GER05.kra.c
OK-GER05  < ..\..\..\t\sixnum.txt  > OK-Out05.txt

gcc  -IC:\ProgFiles\MinGW\include  -o OK-GER11.exe OK-GER11.kra.c
OK-GER11  < ..\..\..\..\t\30-enters.txt > OK-Out11.txt


type OK-Out05.txt >> z.txt
type OK-Out11.txt >> z.txt


del *.tds
del *.obj
del *.bak

