package logic.proof.builder.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

public class InputSentenceEditText extends EditText {

    public InputSentenceEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InputSentenceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputSentenceEditText(Context context) {
        super(context);
    }
    
    public boolean deleteSurroundingText(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
    	
            // Un-comment if you wish to cancel the backspace:
            // return false;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new ZanyInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class ZanyInputConnection extends InputConnectionWrapper {

        public ZanyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
        	InputSentenceActivity.clickBackspaceButton(null);
        	
                // Un-comment if you wish to cancel the backspace:
                return false;
            }
            return super.sendKeyEvent(event);
        }
        
 
        

    }

}
