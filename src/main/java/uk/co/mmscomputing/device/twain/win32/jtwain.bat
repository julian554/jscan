rem Need to set path in autoexec.bat
rem set path=%path%;c:\Borland\Bcc55\Bin 
cd J:\uk\co\mmscomputing\device\twain\win32
bcc32 -w-par -tWD -I"c:\Borland\Bcc55\include" -L"c:\Borland\Bcc55\lib;@cdk\lib\psdk" -L"c:\Borland\Bcc55\lib" -e"jtwain.dll" *.cpp