package cn.local.psqirecord;
import android.content.Context;import android.graphics.*;import android.graphics.drawable.Drawable;
/** Four simple, local vector navigation symbols. No network or raster assets. */
public final class UiIcon extends Drawable {
 private final int kind,size;private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
 public UiIcon(Context context,int kind,int color){this.kind=kind;size=Math.round(22*context.getResources().getDisplayMetrics().density);p.setColor(color);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(1.7f);p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);setBounds(0,0,size,size);}
 @Override public int getIntrinsicWidth(){return size;}@Override public int getIntrinsicHeight(){return size;}
 @Override public void draw(Canvas canvas){canvas.save();canvas.translate(getBounds().left,getBounds().top);canvas.scale(getBounds().width()/24f,getBounds().height()/24f);if(kind==0){canvas.drawCircle(12,7,3,p);canvas.drawRoundRect(5,13,19,21,4,4,p);}else if(kind==1){canvas.drawLine(4,21,21,21,p);canvas.drawLine(6,17,6,11,p);canvas.drawLine(12,17,12,4,p);canvas.drawLine(18,17,18,8,p);}else if(kind==2){canvas.drawRoundRect(3,4,21,21,2,2,p);canvas.drawLine(8,4,8,10,p);canvas.drawLine(8,10,16,10,p);canvas.drawLine(16,10,16,4,p);canvas.drawRect(7,15,17,21,p);}else{canvas.drawCircle(5,12,1.5f,p);canvas.drawCircle(12,12,1.5f,p);canvas.drawCircle(19,12,1.5f,p);}canvas.restore();}
 @Override public void setAlpha(int a){p.setAlpha(a);}@Override public void setColorFilter(ColorFilter f){p.setColorFilter(f);}@Override public int getOpacity(){return PixelFormat.TRANSLUCENT;}
}
