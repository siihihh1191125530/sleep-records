package cn.local.psqirecord;

import java.util.*;

public final class Form {
 public static final String RULE = "PSQI-user-20261113-v1";
 public static final String[] FACTORS={"主观睡眠质量","睡眠潜伏期","睡眠持续性","习惯性睡眠效率","睡眠紊乱","使用睡眠药物","白天功能紊乱"};
 public static final String[] KEYS={"patient","q1","q2","q3","q4","q5a","q5b","q5c","q5d","q5e","q5f","q5g","q5h","q5i","q5j","q5jText","q6","q7","q8","q9","note"};
 public static final String[] FREQ={"无","不足1次／周","1—2次／周","3次或以上／周"};
 public static final String[] DISTURB={"入睡困难（30分钟内不能入睡）","夜间易醒或早醒","夜间去厕所","呼吸不畅","大声咳嗽或鼾声高","感觉冷","感觉热","做噩梦","疼痛不适","其他影响睡眠的事情"};
 public static Map<String,String> empty(){Map<String,String> a=new LinkedHashMap<>();for(String k:KEYS)a.put(k,"");return a;}
 public static String get(Map<String,String>a,String k){String v=a.get(k);return v==null?"":v;}
 public static int time(String s){if(!s.matches("(?:[01]\\d|2[0-3]):[0-5]\\d"))throw new IllegalArgumentException("时间格式应为 HH:mm");return Integer.parseInt(s.substring(0,2))*60+Integer.parseInt(s.substring(3));}
 public static double number(String s){if(!s.matches("\\d+(?:\\.\\d{1,2})?"))throw new IllegalArgumentException("请输入非负数字，最多两位小数");double d=Double.parseDouble(s);if(!Double.isFinite(d))throw new IllegalArgumentException("数字无效");return d;}
 public static int choice(Map<String,String>a,String k){String v=get(a,k);if(!v.matches("[0-3]"))throw new IllegalArgumentException("请完成所有选项");return Integer.parseInt(v);}
 public static List<String> errors(Map<String,String>a){
  List<String> e=new ArrayList<>(); if(get(a,"patient").trim().isEmpty())e.add("请填写病人编号");
  int bed=-1,wake=-1;double hours=-1;
  try{bed=time(get(a,"q1"));}catch(Exception ex){e.add("请选择上床时间");}
  try{wake=time(get(a,"q3"));}catch(Exception ex){e.add("请选择起床时间");}
  try{String s=get(a,"q2");if(!s.matches("\\d{1,4}")||Integer.parseInt(s)>1440)throw new Exception();}catch(Exception ex){e.add("入睡时间应为0～1440的整数分钟");}
  try{hours=number(get(a,"q4"));if(hours>24)throw new Exception();}catch(Exception ex){e.add("实际睡眠时间应为0～24小时，最多两位小数");hours=-1;}
  if(bed>=0&&wake>=0){int span=(wake-bed+1440)%1440;if(span==0)e.add("上床与起床时间相同，无法确定卧床时长");else if(hours>=0&&hours*60>span+0.00001)e.add("实际睡眠时长超过卧床时长，请核对");}
  for(char c='a';c<='j';c++)try{choice(a,"q5"+c);}catch(Exception ex){e.add("请选择第5"+c+"项频率");}
  for(int n=6;n<=9;n++)try{choice(a,"q"+n);}catch(Exception ex){e.add("请完成第"+n+"题");}
  if(!get(a,"q5j").isEmpty()&&!get(a,"q5j").equals("0")&&get(a,"q5jText").trim().isEmpty())e.add("第5j项选择了发生频率，请填写具体事情");
  return e;
 }
 public static int latency(int minutes){return minutes<=15?0:minutes<=30?1:minutes<=60?2:3;}
 public static int duration(double hours){return hours>7?0:hours>=6?1:hours>=5?2:3;}
 public static int efficiency(double percent){return percent>=85?0:percent>=75?1:percent>=65?2:3;}
 public static int pair(int sum){return sum==0?0:sum<=2?1:sum<=4?2:3;}
 public static int disturbance(int sum){return sum==0?0:sum<=9?1:sum<=18?2:3;}
 public static int[] score(Map<String,String>a){List<String> e=errors(a);if(!e.isEmpty())throw new IllegalArgumentException(e.get(0));int sum=0;for(char c='b';c<='j';c++)sum+=choice(a,"q5"+c);return new int[]{choice(a,"q6"),pair(latency(Integer.parseInt(get(a,"q2")))+choice(a,"q5a")),duration(number(get(a,"q4"))),efficiency(efficiencyValue(a)),disturbance(sum),choice(a,"q7"),pair(choice(a,"q8")+choice(a,"q9"))};}
 public static double efficiencyValue(Map<String,String>a){return number(get(a,"q4"))*6000/((time(get(a,"q3"))-time(get(a,"q1"))+1440)%1440);}
 public static int total(int[] s){int n=0;for(int v:s)n+=v;return n;}
 public static String RULES="本版规则："+RULE+"\n\n根据提供的 PSQI.doc 和2026-11-13确认的边界计分。\n\n1. 主观睡眠质量：第6题0～3分。\n2. 入睡时间：≤15分钟=0，16～30=1，31～60=2，>60=3；与5a相加后，0=0分、1～2=1分、3～4=2分、5～6=3分。\n3. 睡眠时长：>7小时=0，6～7=1，5～不足6=2，<5=3。\n4. 睡眠效率：实际睡眠÷卧床时间×100%；≥85%=0，75～不足85%=1，65～不足75%=2，<65%=3。卧床时间按24小时跨午夜计算，不对效率先取整。\n5. 睡眠紊乱：5b～5j相加，0=0分、1～9=1分、10～18=2分、19～27=3分。\n6. 催眠药物：第7题0～3分。\n7. 白天功能：第8、9题相加，按0、1～2、3～4、5～6对应0～3分。\n\n全部有效答案填写后，七项相加得到0～21分。未填项不当作0；5j有发生频率时必须填说明。分数不自动转成诊断。\n\n参考：Buysse等，Psychiatry Research 28 (1989), 193–213。原文件的睡眠时长笔误与边界遗漏按用户确认规则处理。";
}
