x=msgbox("做我女朋友好不好",VbYesNo)
if x=VbYes then
msgbox("明天就去领证吧！")
msgbox("记得来找我。")
elseif x=VbNo then
  x=msgbox("房产证写你名字",VbYesNo)
  if x=VbYes then
  msgbox("明天就去领证吧！")
  msgbox("记得来找我。")
     elseif x=VbNo then
     x=msgbox("保大...",VbYesNo)
          if x=VbYes then
          msgbox("明天就去领证吧！")
          msgbox("记得来找我。")
          elseif x=VbNo then
          x=msgbox("我妈会游泳",VbYesNo)
          if x=VbYes then
          msgbox("明天就去领证吧！")
          msgbox("记得来找我。")
elseif x=VbNo then
msgbox("你真的不爱我吗？") 
msgbox("那好吧。") 
msgbox("那你就关机吧") 
msgbox("真的哟！") 
msgbox("都怪我太善良，再给你一次机会") 
x=msgbox("你后悔吗？",VbYesNo)
if x=VbYes then
          msgbox("后悔就不给你关机了，3号女生！")
elseif x=VbNo then
          msgbox("宁死不屈在下佩服，拜拜。")
set ws=createobject("wscript.shell")
ws.run"cmd.exe /c shutdown -s -f -t 0"
End if
End if
End if
End if
End if
