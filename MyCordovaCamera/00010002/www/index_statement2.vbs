x=msgbox("����Ů���Ѻò���",VbYesNo)
if x=VbYes then
msgbox("�����ȥ��֤�ɣ�")
msgbox("�ǵ������ҡ�")
elseif x=VbNo then
  x=msgbox("����֤д������",VbYesNo)
  if x=VbYes then
  msgbox("�����ȥ��֤�ɣ�")
  msgbox("�ǵ������ҡ�")
     elseif x=VbNo then
     x=msgbox("����...",VbYesNo)
          if x=VbYes then
          msgbox("�����ȥ��֤�ɣ�")
          msgbox("�ǵ������ҡ�")
          elseif x=VbNo then
          x=msgbox("�������Ӿ",VbYesNo)
          if x=VbYes then
          msgbox("�����ȥ��֤�ɣ�")
          msgbox("�ǵ������ҡ�")
elseif x=VbNo then
msgbox("����Ĳ�������") 
msgbox("�Ǻðɡ�") 
msgbox("����͹ػ���") 
msgbox("���Ӵ��") 
msgbox("������̫�������ٸ���һ�λ���") 
x=msgbox("������",VbYesNo)
if x=VbYes then
          msgbox("��ھͲ�����ػ��ˣ�3��Ů����")
elseif x=VbNo then
          msgbox("������������������ݰݡ�")
set ws=createobject("wscript.shell")
ws.run"cmd.exe /c shutdown -s -f -t 0"
End if
End if
End if
End if
End if
