import cn.local.psqirecord.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class CoreTest {
 static int checks=0;
 static void check(boolean ok,String message){checks++;if(!ok)throw new AssertionError(message);}
 public static void main(String[]args)throws Exception{
  Map<String,String>a=Form.empty();a.put("patient","TEST-001");a.put("q1","22:00");a.put("q3","06:00");a.put("q2","15");a.put("q4","8");for(char c='a';c<='j';c++)a.put("q5"+c,"0");for(int i=6;i<=9;i++)a.put("q"+i,"0");check(Form.errors(a).isEmpty(),"valid all-zero case");check(Form.total(Form.score(a))==0,"zero score");check(Form.efficiencyValue(a)==100,"overnight duration");
  double[] durations={7.01,7,6.99,6,5.99,5,4.99,0};int[] ds={0,1,1,1,2,2,3,3};for(int i=0;i<ds.length;i++)check(Form.duration(durations[i])==ds[i],"duration "+durations[i]);
  int[] mins={0,15,16,30,31,60,61};int[] ms={0,0,1,1,2,2,3};for(int i=0;i<ms.length;i++)check(Form.latency(mins[i])==ms[i],"latency "+mins[i]);
  double[] eff={100,85,84.99,75,74.99,65,64.99,0};int[] es={0,0,1,1,2,2,3,3};for(int i=0;i<es.length;i++)check(Form.efficiency(eff[i])==es[i],"efficiency "+eff[i]);
  for(int i=0;i<=6;i++)check(Form.pair(i)==new int[]{0,1,1,2,2,3,3}[i],"pair "+i);
  for(int i=0;i<=27;i++)check(Form.disturbance(i)==(i==0?0:i<=9?1:i<=18?2:3),"disturbance "+i);
  a.put("q4","4");a.put("q2","61");for(char c='a';c<='j';c++)a.put("q5"+c,"3");a.put("q5jText","其他原因");for(int i=6;i<=9;i++)a.put("q"+i,"3");check(Form.total(Form.score(a))==21,"maximum score");
  a.put("q5a","");check(!Form.errors(a).isEmpty(),"missing is not zero");a.put("q5a","3");a.put("q5jText","");check(!Form.errors(a).isEmpty(),"other cause required");a.put("q5jText","原因");a.put("q4","9");check(!Form.errors(a).isEmpty(),"sleep exceeds bed");a.put("q4","4");a.put("q3","22:00");check(!Form.errors(a).isEmpty(),"equal clock times invalid");a.put("q1","00:30");a.put("q3","08:30");check(Form.efficiencyValue(a)==50,"same-day duration");
  for(String bad:new String[]{"NaN","Infinity","-1","","1.234","1e2"}){boolean failed=false;try{Form.number(bad);}catch(Exception e){failed=true;}check(failed,"reject bad number "+bad);}
  String plain="病人编号与答案备份\nTEST ONLY";byte[] enc=BackupFile.encode(plain);check(plain.equals(BackupFile.decode(enc)),"password-free round trip");check(new String(enc,StandardCharsets.UTF_8).contains(plain),"backup is unencrypted");enc[enc.length-1]^=1;boolean tampered=false;try{BackupFile.decode(enc);}catch(Exception e){tampered=true;}check(tampered,"corrupted backup rejected");
  for(int len:new int[]{0,8,35,51}){boolean failed=false;try{BackupFile.decode(new byte[len]);}catch(Exception e){failed=true;}check(failed,"truncated backup "+len);}
  System.out.println("PASS: "+checks+" core assertions (scoring, input validation, password-free backup checksums)");
 }
}
