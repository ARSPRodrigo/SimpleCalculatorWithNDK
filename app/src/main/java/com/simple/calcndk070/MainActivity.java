package com.simple.calcndk070;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    private TextView textView;
    private String number1 = "";
    private String number2 = "";
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
                handleDigit( tag );
                break;
            case ".":
                handleDot( tag );
                break;
            case "+":
            case "-":
            case "*":
            case "÷":
            case "^":
            case "sqrt":
                handleOperator( tag );
                break;
            case "=":
                handleCalculation();
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
                number1 = appendDigit( number1, digit );
            }
            else
            {
                number1 = digit;
                result = "";
            }
        }
        else
        {
            number2 = appendDigit( number2, digit );
        }
        refreshTextView();
    }

    private void handleDot( String dot )
    {
        if( operator.isEmpty() && result.isEmpty() )
        {
            number1 = appendDot( dot, number1 );
        }
        else if( operator.isEmpty() )
        {
            result = "";
            number1 = "0" + dot;
        }
        else
        {
            number2 = appendDot( dot, number2 );
        }
        refreshTextView();
    }

    private String appendDot( String dot, String number )
    {
        if( number.isEmpty() )
        {
            number = "0" + dot;
        }
        else
        {
            number += dot;
        }
        return number;
    }

    private String appendDigit( String number, String digit )
    {
        if( !number.isEmpty() && !number.equals( "0." ) && Double.parseDouble( number ) == 0 )
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
        if( operator.equals( "sqrt" ) )
        {
            sqrt();
        }
        else
        {
            if( number1.isEmpty() )
            {
                number1 = "0";
            }
            else if( !this.operator.isEmpty() && !number2.isEmpty() )
            {
                handleCalculation();
                number1 = result;
                refreshTextView();
            }
            this.operator = operator;
        }
        refreshTextView();
    }

    private void sqrt()
    {
        if( !number1.isEmpty() && !operator.isEmpty() && !number2.isEmpty() )
        {
            handleCalculation();
            result = formatOutput( String.valueOf( sqrtFromJNI( Double.parseDouble( number1 ) ) ) );
            number1 = result;
        }
        else if( !number1.isEmpty() )
        {
            result = formatOutput( String.valueOf( sqrtFromJNI( Double.parseDouble( number1 ) ) ) );
            number1 = result;
            operator = "";
        }
    }

    private void handleCalculation()
    {
        if( !number1.isEmpty() && !operator.isEmpty() && !number2.isEmpty() )
        {
            double num1 = Double.parseDouble( this.number1 );
            double num2 = Double.parseDouble( number2 );
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
                case "÷":
                    ans = divideFromJNI( num1, num2 );
                    break;
                case "^":
                    ans = powerFromJNI( num1, num2 );
                    break;
                default:
                    handleError();
            }
            result = formatOutput( Double.toString( ans ) );
            this.number1 = result;
            number2 = "";
            operator = "";
            refreshTextView();
        }
    }

    private void handleClear()
    {
        number1 = "";
        number2 = "";
        operator = "";
        result = "";
        textView.setText( "" );
    }

    private void handleBack()
    {
        if( !number1.isEmpty() && operator.isEmpty() )
        {
            number1 = number1.substring( 0, number1.length() - 1 );
            refreshTextView();
        }
        else if( !operator.isEmpty() && number2.isEmpty() )
        {
            operator = "";
            refreshTextView();
        }
        else
        {
            number2 = number2.substring( 0, number2.length() - 1 );
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
            textView.setText( number1 );
        }
        else if( number2.isEmpty() )
        {
            textView.setText( number1 + ( operator.equals( "*" ) ? "×" : operator ) );
        }
        else
        {
            textView.setText( number1 + ( operator.equals( "*" ) ? "×" : operator ) + number2 );
        }
    }

    private String formatOutput( String output )
    {
        if( output.length() > 14 )
        {
            output = output.substring( 0, 14 ); // max chars in the text view
        }
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
