package com.simple.calcndk070;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class ExampleUnitTest
{
    MainActivity instance;

    @Before
    public void setUp()
    {
        MainActivity.loadLib = false;
        instance = spy( MainActivity.class );
        doNothing().when( instance ).refreshTextView(); // Hence the refresh text view contains UI actions, it is mocked
    }

    @Test
    public void addition_isCorrect()
    {
        assertEquals( 4, 2 + 2 );
    }

    @Test
    public void handleDigitTest()
    {
        // mock append digit method
        doAnswer( new Answer()
        {

            @Override
            public Object answer( InvocationOnMock inv ) throws Throwable
            {
                Object arg0 = inv.getArgumentAt( 0, String.class );
                Object arg1 = inv.getArgumentAt( 1, String.class );
                String num = arg0.toString();
                String digit = arg1.toString();

                return num + digit;
            }
        } ).when( instance ).appendDigit( any( String.class ), any( String.class ) );

        instance.operator = "";
        instance.result = "";
        instance.number1 = "";
        String[] digits = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        String[] answers = { "1", "12", "123", "1234", "12345", "123456", "1234567", "12345678", "123456789" };
        for( int i = 0; i < digits.length; i++ )
        {
            String digit = digits[i];
            instance.handleDigit( digit );
            assertEquals( answers[i], instance.number1 );
        }

        instance.result = "5";
        String digit = "9";
        instance.handleDigit( digit );
        assertTrue( instance.result.isEmpty() );

        instance.operator = "+";
        digit = "8";
        instance.handleDigit( digit );
        assertEquals( digit, instance.number2 );
    }

    @Test
    public void handleDotTest()
    {
        // mock append digit method
        doAnswer( new Answer()
        {

            @Override
            public Object answer( InvocationOnMock inv ) throws Throwable
            {
                Object arg0 = inv.getArgumentAt( 0, String.class );
                Object arg1 = inv.getArgumentAt( 1, String.class );
                String dot = arg0.toString();
                String number = arg1.toString();
                if( number.contains( "." ) )
                {
                    return number;
                }
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
        } ).when( instance ).appendDot( any( String.class ), any( String.class ) );

        instance.operator = "";
        instance.result = "";
        String[] numbers = { "", "1", "1." };
        String[] answers = { "0.", "1.", "1." };
        for( int i = 0; i < numbers.length; i++ )
        {
            instance.number1 = numbers[i];
            instance.handleDot( "." );
            assertEquals( answers[i], instance.number1 );
        }

        instance.operator = "";
        instance.result = "10";
        instance.handleDot( "." );
        assertEquals( "0.", instance.number1 );

        instance.number1 = "1";
        instance.operator = "+";
        instance.number2 = "8";
        String expected = "8.";
        instance.handleDot( "." );
        assertEquals( expected, instance.number2 );


    }

    @Test
    public void handleOperatorTest()
    {
        doAnswer( new Answer()
        {

            @Override
            public Object answer( InvocationOnMock inv ) throws Throwable
            {
                return "calc";
            }
        } ).when( instance ).handleCalculation();

        doAnswer( new Answer()
        {

            @Override
            public Object answer( InvocationOnMock inv ) throws Throwable
            {
                instance.operator = "sqrt";
                return null;
            }
        } ).when( instance ).sqrt();

        String[] operators = { "+", "-", "*", "÷", "^", "sqrt" };
        String[] answers = { "+", "-", "*", "÷", "^", "sqrt" };
        for( int i = 0; i < operators.length; i++ )
        {
            instance.handleOperator( operators[i] );
            assertEquals( answers[i], instance.operator );
        }

        for( int i = 0; i < operators.length - 1; i++ )
        {
            instance.number1 = "";
            instance.number2 = "";
            instance.operator = "";
            instance.handleOperator( operators[i] );
            assertEquals( "0", instance.number1 );
        }
    }

    @Test
    public void appendDigitTest()
    {
        String[] numbers = { "", "0", "0.", "1" };
        String digit = "9";
        String[] answers = { "9", "9", "0.9", "19" };
        for( int i = 0; i < numbers.length; i++ )
        {
            String answer = instance.appendDigit( numbers[i], digit );
            assertEquals( answers[i], answer );
        }
    }

    @Test
    public void appendDotTest()
    {
        String[] numbers = { "1.", "", "0" };
        String dot = ".";
        String[] answers = { "1.", "0.", "0." };
        for( int i = 0; i < numbers.length; i++ )
        {
            String answer = instance.appendDot( dot, numbers[i] );
            assertEquals( answers[i], answer );
        }
    }

    @Test
    public void formatOutputTest()
    {
        double[] outputs = { 12345678901234.0, 1.23456789012345, 1.0 };
        String[] answers = { "1.23456789E13", "1.234567890123", "1" };

        for( int i = 0; i < outputs.length; i++ )
        {
            String answer = instance.formatOutput( Double.toString( outputs[i] ) );
            assertEquals( answers[i], answer );
        }
    }

    @Test
    public void handleClearTest()
    {
        doNothing().when( instance ).setText( any( String.class ) );

        instance.number1 = "1";
        instance.number2 = "2";
        instance.operator = "+";
        instance.result = "3";
        instance.handleClear();
        assertTrue( instance.number1.isEmpty() );
        assertTrue( instance.number2.isEmpty() );
        assertTrue( instance.operator.isEmpty() );
        assertTrue( instance.result.isEmpty() );

    }

    @Test
    public void handleBackTest()
    {
        instance.number1 = "100";
        instance.handleBack();
        assertEquals( "10", instance.number1 );
        instance.operator = "+";
        instance.handleBack();
        assertEquals( "10", instance.number1 );
        assertEquals( "", instance.operator );
        instance.number2 = "20";
        instance.operator = "+";
        instance.handleBack();
        assertEquals( "10", instance.number1 );
        assertEquals( "+", instance.operator );
        assertEquals( "2", instance.number2 );
        instance.result = "30";
        instance.number2 = "";
        instance.operator = "";
        instance.number1 = instance.result;
        instance.handleBack();
        assertEquals( "3", instance.number1 );
    }
}