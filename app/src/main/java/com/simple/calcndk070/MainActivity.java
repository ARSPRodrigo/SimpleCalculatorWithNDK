package com.simple.calcndk070;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    private TextView textView;
    private String num1String = "";
    private String num2String = "";
    private String operator = "";
    private String result = "";

    // Used to load the 'native-lib' library on application startup.

    static
    {
        System.loadLibrary( "native-lib" );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Example of a call to a native method
        textView = findViewById( R.id.sample_text );
        textView.setText( stringFromJNI() );
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native double addFromJNI( double num1, double num2 );

    public native double subtractFromJNI( double num1, double num2 );

    public native double multiplyFromJNI( double num1, double num2 );

    public native double divideFromJNI( double num1, double num2 );

    public native double powerFromJNI( double num1, double num2 );

    public native double sqrtFromJNI( double num );

    public void buttonPress( View view )
    {
        String tag = ( String ) view.getTag();

        switch( tag )
        {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case ".":
                handleDigit( tag );
                break;
            case "+":
            case "-":
            case "*":
            case "/":
            case "^":
                handleOperator( tag );
                break;
            case "sqrt":
                handleSqrt();
                break;
            case "=":
                calculate();
                break;
            case "C":
                handleClear();
                break;
            case "bk":
                handleBack();
                break;
            default:
                handleError();
        }
    }


    private void handleDigit( String digit )
    {
        if( operator.isEmpty() )
        {
            if( result.isEmpty() )
            {
                num1String = appendDigit( num1String, digit );
            }
            else
            {
                num1String = digit;
                result = "";
            }
        }
        else
        {
            num2String = appendDigit( num2String, digit );
        }
        refreshTextView();
    }

    private String appendDigit( String number, String digit )
    {
        if( !number.isEmpty() && !number.equals( "." ) && Double.parseDouble( number ) == 0 && !digit.equals( "." ) )
        {
            number = digit;
        }
        else
        {
            number = number + digit;
        }
        return number;
    }

    private void handleOperator( String operator )
    {
        if( num1String.isEmpty() )
        {
            num1String = "0";
        }
        else if( !this.operator.isEmpty() && !num2String.isEmpty() )
        {
            calculate();
            num1String = result;
            refreshTextView();
        }
        this.operator = operator;
        refreshTextView();
    }

    private void handleSqrt()
    {
        if( !num1String.isEmpty() && !operator.isEmpty() && !num2String.isEmpty() )
        {
            calculate();
            result = formatOutput( String.valueOf( sqrtFromJNI( Double.parseDouble( num1String ) ) ) );
            num1String = result;
            refreshTextView();
        }
        else if( !num1String.isEmpty() )
        {
            result = formatOutput( String.valueOf( sqrtFromJNI( Double.parseDouble( num1String ) ) ) );
            num1String = result;
            operator = "";
            refreshTextView();
        }
    }

    private void calculate()
    {
        if( !num1String.isEmpty() && !operator.isEmpty() && !num2String.isEmpty() )
        {
            double num1 = Double.parseDouble( num1String );
            double num2 = Double.parseDouble( num2String );
            double ans = 0;
            switch( operator )
            {
                case "+":
                    ans = addFromJNI( num1, num2 );
                    break;
                case "-":
                    ans = subtractFromJNI( num1, num2 );
                    break;
                case "*":
                    ans = multiplyFromJNI( num1, num2 );
                    break;
                case "/":
                    ans = divideFromJNI( num1, num2 );
                    break;
                case "^":
                    ans = powerFromJNI( num1, num2 );
                    break;
                default:
                    handleError();
            }
            result = formatOutput( Double.toString( ans ) );
            num1String = result;
            num2String = "";
            operator = "";
            refreshTextView();
        }
    }

    private void handleClear()
    {
        num1String = "";
        num2String = "";
        operator = "";
        result = "";
        textView.setText( "" );
    }

    private void handleBack()
    {
        if( !num1String.isEmpty() && operator.isEmpty() )
        {
            num1String = num1String.substring( 0, num1String.length() - 1 );
            refreshTextView();
        }
        else if( !operator.isEmpty() && num2String.isEmpty() )
        {
            operator = "";
            refreshTextView();
        }
        else
        {
            num2String = num2String.substring( 0, num2String.length() - 1 );
            refreshTextView();
        }
    }

    @SuppressLint( "SetTextI18n" )
    private void handleError()
    {
        textView.setText( "Error." );
    }

    @SuppressLint( "SetTextI18n" )
    private void refreshTextView()
    {
        if( operator.isEmpty() )
        {
            textView.setText( num1String );
        }
        else if( num2String.isEmpty() )
        {
            textView.setText( num1String + ( operator.equals( "*" ) ? "×" : operator ) );
        }
        else
        {
            textView.setText( num1String + ( operator.equals( "*" ) ? "×" : operator ) + num2String );
        }
    }

    private String formatOutput( String output )
    {
        if( output.split( "\\." )[1].equals( "0" ) )
        {
            return output.split( "\\." )[0];
        }
        else
        {
            return output;
        }
    }
}
