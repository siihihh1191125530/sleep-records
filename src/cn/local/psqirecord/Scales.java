package cn.local.psqirecord;

import java.util.*;

/** Item-level data entry definitions. Original sources and implementation choices are explicit. */
public final class Scales {
 public static final String[] IDS={"PSQI","NRS","AIS8","HAMD17","HAMA14"};
 public static final class Item {
  public final String key,title,help;public final String[] options;
  Item(int number,String title,String help,String... options){this.key="i"+number;this.title=title;this.help=help;this.options=options;}
  public int max(){return options.length-1;}
 }
 public static final class Def {
  public final String id,title,shortName,period,rule,notes;public final int max;public final boolean clinician;public final Item[] items;
  Def(String id,String title,String shortName,String period,String rule,int max,boolean clinician,String notes,Item... items){this.id=id;this.title=title;this.shortName=shortName;this.period=period;this.rule=rule;this.max=max;this.clinician=clinician;this.notes=notes;this.items=items;}
 }
 private static Item item(int n,String title,String help,String... labels){return new Item(n,title,help,labels);}
 private static Item anxiety(int n,String title,String help){return item(n,title,help,"无","轻度","中度","重度","极重度");}
 private static final Map<String,Def> ALL=new LinkedHashMap<>();
 static {
  ALL.put("PSQI",new Def("PSQI","PSQI 睡眠质量","PSQI","最近1个月",Form.RULE,21,false,"中文题目沿用提供的PSQI.doc；按2026-11-13确认的边界计分。不同于无差异照录官方程序，详见来源与计分说明。"));
  ALL.put("NRS",new Def("NRS","NRS 疼痛评分","NRS","当前疼痛","NRS11-current-20261114-v1",10,false,"由患者选择0～10的整数。0表示不痛，10表示能想象的最剧烈疼痛。本项目固定记录当前疼痛；部位、活动或静息等情境可写入备注。",
   item(1,"当前疼痛程度","请患者选择一个最接近当前疼痛程度的数字。","0 · 无痛","1","2","3","4","5","6","7","8","9","10 · 最剧烈疼痛")));
  ALL.put("AIS8",new Def("AIS8","AIS-8 失眠评分","AIS-8","最近1个月（每周至少3次的睡眠困扰）","AIS8-sum-20261114-v1",24,false,"八个条目各0～3分，直接相加。本界面提供中文条目及简明录入提示，非宣称经过独立验证或授权的电子中文版；正式施测请核对来源中的原表。",
   item(1,"入睡所需时间","熄灯准备睡眠后，是否难以入睡？","无困难","稍有延迟","明显延迟","严重延迟或整夜未眠"),
   item(2,"夜间睡眠中断","夜间醒来对睡眠的影响。","无影响","轻微影响","明显影响","严重影响或未能入睡"),
   item(3,"早于期望时间醒来","是否比希望起床的时间过早醒来？","没有提前","略早","明显提前","严重提前或未眠"),
   item(4,"总睡眠时长","相对于个人需要，睡眠是否足够？","足够","稍显不足","明显不足","严重不足或未眠"),
   item(5,"整体睡眠质量","不单看睡眠时长，评价总体睡眠质量。","满意","稍不满意","明显不满意","很不满意或未眠"),
   item(6,"白天的舒适感","白天总体的身心感受。","正常","轻微下降","明显下降","严重下降"),
   item(7,"白天的身心功能","白天身体活动和思考、注意等功能。","正常","轻微受影响","明显受影响","严重受影响"),
   item(8,"白天困倦","白天嗜睡或困倦的程度。","没有","轻微","明显","严重")));
  ALL.put("HAMD17",new Def("HAMD17","HAMD-17 抑郁评分","HAMD-17","最近1周","HAMD17-16a-20261114-v1",52,true,"由受过训练的评估人员结合访谈与观察评分。各题有不同分值上限。第16题固定使用患者叙述法16a，不与称重法16b同时累加；未评定应留空，不能把“未评定”当成3分。中文为原表的录入辅助译述。",
   item(1,"抑郁情绪","悲伤、无望、无助或无价值感。","未见","询问后才表达","会主动诉说","表情、姿态、声音或哭泣明显流露","言语与非言语表达几乎始终被这种情绪占据"),
   item(2,"罪恶感","根据自责、罪恶观念及相关精神症状评定。","没有","自责，认为辜负别人","反复纠结以往过错或有罪恶观念","把患病视为惩罚，或有罪恶妄想","有指责、谴责性幻听或威胁性幻视"),
   item(3,"自杀相关表现","依据临床访谈评定，不以总分替代此项的临床判断。","没有","认为生活不值得继续","希望自己已死，或想到自己的死亡","有自杀念头或姿态","出现自杀企图，任何严重企图均记4分"),
   item(4,"入睡困难","评价夜间刚开始睡眠时的困难。","没有","偶有困难，例如超过半小时才入睡","每晚均有入睡困难"),
   item(5,"夜间睡眠不稳","夜间睡眠中断；单纯起床排尿不按2分处理。","没有","诉夜间不安、睡眠受扰","夜间醒来或起床（排尿除外）"),
   item(6,"早醒","清晨醒来后是否能再睡。","没有","早醒但能再入睡","醒来或起床后无法再入睡"),
   item(7,"工作与活动","根据工作、兴趣、活动时间及生活事务的变化评定。","没有困难","活动、工作或爱好伴无能感、疲劳或虚弱","对工作、活动或爱好失去兴趣，需要勉强自己","活动时间或效率下降；除日常事务外每天活动不足3小时","因病停止工作，几乎无日常事务外活动或日常事务也需帮助"),
   item(8,"精神运动迟滞","思维、言语、注意与动作速度的观察。","言语思维正常","访谈中轻度迟缓","访谈中明显迟缓","访谈进行困难","完全木僵"),
   item(9,"激越","观察坐立不安或反复动作。","没有","有小动作、烦躁不安","摆弄手或头发等","走动，难以安坐","绞手、咬指甲、扯头发或咬嘴唇"),
   item(10,"精神性焦虑","紧张、易激惹、担忧或恐惧。","没有","主观紧张、易激惹","为小事担心","表情或言语明显显露忧惧","未被问及也主动流露恐惧"),
   item(11,"躯体性焦虑","焦虑伴随的胃肠、心血管、呼吸、排尿或出汗等表现。","没有","轻度","中度","重度","严重到影响能力"),
   item(12,"胃肠道躯体症状","食欲、进食和胃肠不适。","没有","食欲下降但无需督促进食，或腹部沉重","需督促进食，或请求/需要胃肠、通便药物"),
   item(13,"一般躯体症状","肢体、背部、头部沉重；疼痛、精力下降或易疲劳。","没有","存在上述不适","上述症状明确明显"),
   item(14,"生殖系统症状","例如性欲减退、月经相关异常。","没有","轻度","重度"),
   item(15,"疑病","对身体和健康的关注程度。","没有","过度关注自己的身体","专注担忧健康","反复诉述、求助等","疑病妄想"),
   item(16,"体重减轻（患者叙述法16a）","固定使用患者对本次疾病中体重变化的叙述；不能评定则留空。","没有体重减轻","可能因本次疾病而减轻","明确体重减轻"),
   item(17,"自知力","对自身患病状态的认识。","承认自己抑郁并患病","承认有病，但归因为饮食、气候、劳累、感染等","完全否认患病")));
  ALL.put("HAMA14",new Def("HAMA14","HAMA-14 焦虑评分","HAMA-14","最近1周及访谈时表现","HAMA14-sum-20261114-v1",56,true,"由评估人员访谈并观察后评分。14项均为0～4分，总分0～56。本项目固定使用最近1周；第14项依据访谈时观察。中文为公开原表的录入辅助译述。",
   anxiety(1,"焦虑心境","担忧、预感坏事、恐惧性预期、易激惹。"),
   anxiety(2,"紧张","紧绷、易疲劳、易惊、易哭、发抖、坐立不安或不能放松。"),
   anxiety(3,"害怕","如害怕黑暗、陌生人、独处、动物、交通或人群。"),
   anxiety(4,"失眠","入睡困难、睡眠中断、不解乏、醒后疲劳、梦多、噩梦或夜惊。"),
   anxiety(5,"认知方面","注意难以集中、记忆变差。"),
   anxiety(6,"抑郁心境","兴趣与愉悦感减少、情绪低落、早醒或日内情绪波动。"),
   anxiety(7,"肌肉系统症状","肌肉疼痛、抽动、僵硬、肌阵挛、磨牙、声音不稳或肌张力增高。"),
   anxiety(8,"感觉系统症状","耳鸣、视物模糊、冷热感、虚弱或针刺感。"),
   anxiety(9,"心血管系统症状","心动过速、心悸、胸痛、血管搏动感、晕厥感或漏搏感。"),
   anxiety(10,"呼吸系统症状","胸部压迫或紧缩、窒息感、叹息或呼吸困难。"),
   anxiety(11,"胃肠系统症状","吞咽困难、腹胀腹痛、烧灼感、恶心呕吐、肠鸣、腹泻、便秘或体重下降。"),
   anxiety(12,"泌尿生殖系统症状","尿频尿急、月经异常、性欲减退或其他性功能问题。"),
   anxiety(13,"自主神经症状","口干、潮红、苍白、出汗、眩晕、紧张性头痛或毛发竖起。"),
   anxiety(14,"访谈时行为","观察小动作、踱步、手抖、皱眉、面部紧绷、叹息、呼吸急促、苍白或吞咽动作等。")));
 }
 public static Def get(String id){Def d=ALL.get(id);if(d==null)throw new IllegalArgumentException("不支持的量表："+id);return d;}
 public static List<String> keys(String id){if(id.equals("PSQI"))return Arrays.asList(Form.KEYS);List<String>out=new ArrayList<>(Arrays.asList("patient","rater","assessmentTime","note"));for(Item item:get(id).items)out.add(item.key);return out;}
 public static Map<String,String> empty(String id){if(id.equals("PSQI"))return Form.empty();Map<String,String>a=new LinkedHashMap<>();for(String k:keys(id))a.put(k,"");return a;}
 public static List<String> errors(String id,Map<String,String>a){if(id.equals("PSQI"))return Form.errors(a);Def d=get(id);List<String>out=new ArrayList<>();if(Form.get(a,"patient").trim().isEmpty())out.add("缺少病人编号");if(d.clinician&&Form.get(a,"rater").trim().isEmpty())out.add("请填写评估人员姓名或编号");try{Form.time(Form.get(a,"assessmentTime"));}catch(Exception e){out.add("请记录评估时间");}for(Item q:d.items){String value=Form.get(a,q.key);if(!value.matches("\\d{1,2}")||Integer.parseInt(value)>q.max())out.add("请完成："+q.title);}return out;}
 public static int[] score(String id,Map<String,String>a){if(id.equals("PSQI"))return Form.score(a);List<String>e=errors(id,a);if(!e.isEmpty())throw new IllegalArgumentException(e.get(0));Item[]items=get(id).items;int[]out=new int[items.length];for(int i=0;i<out.length;i++)out[i]=Integer.parseInt(a.get(items[i].key));return out;}
 public static String label(String id,String key){if(id.equals("PSQI")){String[] labels={"病人编号","上床时间","入睡分钟数","起床时间","睡眠小时数","入睡困难频率","夜醒或早醒频率","夜间如厕频率","呼吸不畅频率","咳嗽或鼾声频率","感觉冷频率","感觉热频率","噩梦频率","疼痛不适频率","其他睡眠影响频率","其他情况说明","睡眠质量","使用催眠药频率","困倦频率（用户原表）","精力不足","备注"};for(int i=0;i<Form.KEYS.length;i++)if(Form.KEYS[i].equals(key))return labels[i];}if(key.equals("patient"))return "病人编号";if(key.equals("rater"))return "评估人员";if(key.equals("assessmentTime"))return "评估时间";if(key.equals("note"))return "备注";for(Item i:get(id).items)if(i.key.equals(key))return i.title;return key;}
 public static String[] scoreLabels(String id){if(id.equals("PSQI"))return Form.FACTORS;Item[] items=get(id).items;String[] s=new String[items.length];for(int i=0;i<s.length;i++)s[i]=items[i].title;return s;}
 public static String[][] sources(String id){
  if(id.equals("PSQI"))return new String[][]{{"开发团队原始量表与计分","https://www.sleep.pitt.edu/psqi","Buysse DJ et al. Psychiatry Research. 1989;28:193–213. DOI:10.1016/0165-1781(89)90047-4"},{"官方计分说明","https://www.sleep.pitt.edu/sites/default/files/assets/Instrument%20Materials/Exhibit%20A-PSQI%20scoring.pdf","原文件部分区间未覆盖边界；本软件按用户确认的边界补齐。5j有症状必须填说明。中文为用户所供PSQI.doc，第8题保留其简化表述，未宣称等同授权标准译本。"}};
  if(id.equals("NRS"))return new String[][]{{"中华医学会 疼痛评估量表应用的中国专家共识（2020版）","https://cmab.yiigle.com/uploads/guide_html/疼痛评估量表应用的中国专家共识（2020版）%20-%20中华疼痛学杂志.html","中华疼痛学杂志. 2020;16(3):177–187；表1、数字评定量表及附录4。0～10整数，自评。"}};
  if(id.equals("AIS8"))return new String[][]{{"AIS原始开发与验证研究","https://pubmed.ncbi.nlm.nih.gov/11033374/","Soldatos CR, Dikeos DG, Paparrigopoulos TJ. J Psychosom Res. 2000;48(6):555–560. DOI:10.1016/S0022-3999(00)00095-7"},{"中文版验证研究","https://scholars.lib.ntu.edu.tw/entities/publication/1767b436-077c-44c2-8fec-11a92b07cc0c","Chiang HL et al. Taiwanese J Psychiatry. 2009;23(1):43–52. DOI:10.29478/TJP.200903.0005；用于核对中文版研究存在，不据此宣称本App获授权或完成验证。"},{"中华中医药学会 睡眠科普知识库","https://www.cacm.org.cn/spskl/item/?item_id=10005","第25问：8条目、0～3分、过去1个月、每周至少3次。未将不同研究的诊断阈值自动套用为诊断。"},{"原量表条目参考","https://www.med.upenn.edu/cbti/assets/user-content/documents/Sleep_Instruments_book_3_.pdf","大学托管睡眠量表资料：AIS章节。软件只提供简明中文评分录入提示，正式施测核对原表。"}};
  if(id.equals("HAMD17"))return new String[][]{{"佛罗里达大学精神科 原表","https://dcf.psychiatry.ufl.edu/files/2011/05/HAMILTON-DEPRESSION.pdf","Hamilton M. J Neurol Neurosurg Psychiatry. 1960;23:56–62. DOI:10.1136/jnnp.23.1.56；公开领域原表，17项。第16题“未评定”的编码3不作为有效得分。"},{"美国心理学会 评估工具说明","https://www.apa.org/depression-guideline/assessment","专业人员施测；按前17项计分，存在0～2与0～4两类条目。软件固定使用第16题患者叙述法。"}};
  return new String[][]{{"佛罗里达大学精神科 原表","https://dcf.psychiatry.ufl.edu/files/2011/05/HAMILTON-ANXIETY.pdf","Hamilton M. Br J Med Psychol. 1959;32:50–55. DOI:10.1111/j.2044-8341.1959.tb00467.x；公开领域，14项，每项0～4分。"},{"NIH量表数据元","https://cde.nlm.nih.gov/deView?tinyId=jE1_hJPHEQJ","0无、1轻、2中、3重、4极重；专业人员评定。"},{"过去一周评估窗口的研究实例","https://pmc.ncbi.nlm.nih.gov/articles/PMC9729372/","HAM-A用于最近一周的症状评估；软件固定窗口并在导出中记录，不把填写日期误作一天的回顾范围。"}};
 }
 public static String explanation(String id){Def d=get(id);String s=d.title+"\n回顾范围："+d.period+"\n规则版本："+d.rule+"\n"+d.notes+"\n\n";if(id.equals("PSQI"))s+=Form.RULES;else{s+="有效条目全部完成后直接相加，总分0～"+d.max+"。未填写或未评定不当作0分，不生成完成总分。\n";if(id.equals("HAMD17"))s+="第4、5、6、12、13、14、16、17项为0～2分，其余为0～4分。";}return s;}
}
