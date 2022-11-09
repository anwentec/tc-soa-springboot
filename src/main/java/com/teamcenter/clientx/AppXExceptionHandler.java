//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.clientx;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.teamcenter.schemas.soa._2006_03.exceptions.ConnectionException;
import com.teamcenter.schemas.soa._2006_03.exceptions.InternalServerException;
import com.teamcenter.schemas.soa._2006_03.exceptions.ProtocolException;
import com.teamcenter.soa.client.ExceptionHandler;
import com.teamcenter.soa.exceptions.CanceledOperationException;

/**
 * Implementation of the ExceptionHandler. For ConnectionExceptions (server
 * temporarily down .etc) prompts the user to retry the last request. For other
 * exceptions convert to a RunTime exception.
 */
public class AppXExceptionHandler implements ExceptionHandler
{

    /*
     * (non-Javadoc)
     *
     * @see com.teamcenter.soa.client.ExceptionHandler#handleException(com.teamcenter.schemas.soa._2006_03.exceptions.InternalServerException)
     */
    public void handleException(InternalServerException ise)
    {
        System.out.println("");
        System.out.println("*****");
        System.out
                .println("Exception caught in com.teamcenter.clientx.AppXExceptionHandler.handleException(InternalServerException).");

        LineNumberReader reader = new LineNumberReader(new InputStreamReader(System.in));

        if (ise instanceof ConnectionException)
        {
            // ConnectionException are typically due to a network error (server
            // down .etc) and can be recovered from (the last request can be sent again,
            // after the problem is corrected).
            System.out.print("\nThe server returned an connection error.\n" + ise.getMessage()
                           + "\nDo you wish to retry the last service request?[y/n]");
        }
        else
            if (ise instanceof ProtocolException)
            {
                // ProtocolException are typically due to programming errors
                // (content of HTTP
                // request is incorrect). These are generally can not be
                // recovered from.
                System.out.print("\nThe server returned an protocol error.\n" + ise.getMessage()
                               + "\nThis is most likely the result of a programming error."
                               + "\nDo you wish to retry the last service request?[y/n]");
            }
            else
            {
                System.out.println("\nThe server returned an internal server error.\n"
                                 + ise.getMessage()
                                 + "\nThis is most likely the result of a programming error."
                                 + "\nA RuntimeException will be thrown.");
                throw new RuntimeException(ise.getMessage());
            }

        try
        {
            String retry = reader.readLine();
            // If yes, return to the calling SOA client framework, where the
            // last service request will be resent.
            if (retry.equalsIgnoreCase("y") || retry.equalsIgnoreCase("yes")) return;

            throw new RuntimeException("The user has opted not to retry the last request");
        }
        catch (IOException e)
        {
            System.err.println("Failed to read user response.\nA RuntimeException will be thrown.");
            throw new RuntimeException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.teamcenter.soa.client.ExceptionHandler#handleException(com.teamcenter.soa.exceptions.CanceledOperationException)
     */
    public void handleException(CanceledOperationException coe)
    {
        System.out.println("");
        System.out.println("*****");
        System.out.println("Exception caught in com.teamcenter.clientx.AppXExceptionHandler.handleException(CanceledOperationException).");

        // Expecting this from the login tests with bad credentials, and the
        // AnyUserCredentials class not
        // prompting for different credentials
        throw new RuntimeException(coe);
    }

}
