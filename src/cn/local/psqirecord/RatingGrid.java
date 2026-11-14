package cn.local.psqirecord;
import android.content.Context;import android.view.View;import android.widget.RadioGroup;
/** Wrapping numeric radio buttons, preserving native exclusive selection and semantics. */
public final class RatingGrid extends RadioGroup {
 private int columns=4,cellWidth,rowHeight,gap;
 public RatingGrid(Context c){super(c);gap=Math.round(8*c.getResources().getDisplayMetrics().density);}
 @Override protected void onMeasure(int w,int h){int width=MeasureSpec.getSize(w);float density=getResources().getDisplayMetrics().density,font=getResources().getConfiguration().fontScale;int min=Math.round(60*density*Math.max(1,font*.75f));columns=Math.max(1,Math.min(6,(width+gap)/(min+gap)));cellWidth=Math.max(1,(width-gap*(columns-1))/columns);rowHeight=0;for(int i=0;i<getChildCount();i++){View child=getChildAt(i);child.measure(MeasureSpec.makeMeasureSpec(cellWidth,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));rowHeight=Math.max(rowHeight,child.getMeasuredHeight());}int rows=(getChildCount()+columns-1)/columns;setMeasuredDimension(width,rows*rowHeight+Math.max(0,rows-1)*gap);}
 @Override protected void onLayout(boolean changed,int l,int t,int r,int b){for(int i=0;i<getChildCount();i++){int x=(i%columns)*(cellWidth+gap),y=(i/columns)*(rowHeight+gap);getChildAt(i).layout(x,y,x+cellWidth,y+rowHeight);}}
}
