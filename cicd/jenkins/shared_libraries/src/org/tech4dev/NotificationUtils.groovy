#!/usr/bin/env groovy
package org.tech4dev

class NotificationUtils implements Serializable {
    void sendSuccessNotification(Map config = [:]) {
        List recipients = config.recipients ?: []
        String buildUrl = config.buildUrl ?: 'unknown'
        String subject = config.subject ?: "Build Successful"
        
        if (recipients.size() > 0) {
            // Load email template from resources
            def emailTemplate = libraryResource 'org/tech4dev/templates/email-notification.html'
            
            // Replace placeholders
            emailTemplate = emailTemplate
                .replace('{{BUILD_STATUS}}', 'SUCCESS')
                .replace('{{BUILD_URL}}', buildUrl)
                .replace('{{STATUS_COLOR}}', '#36a64f')
                
            // Send email
            emailext(
                subject: subject,
                body: emailTemplate,
                to: recipients.join(','),
                mimeType: 'text/html'
            )
        }
    }
    
    void sendFailureNotification(Map config = [:]) {
        List recipients = config.recipients ?: []
        String buildUrl = config.buildUrl ?: 'unknown'
        String subject = config.subject ?: "Build Failed"
        
        if (recipients.size() > 0) {
            // Load email template from resources
            def emailTemplate = libraryResource 'org/tech4dev/templates/email-notification.html'
            
            // Replace placeholders
            emailTemplate = emailTemplate
                .replace('{{BUILD_STATUS}}', 'FAILURE')
                .replace('{{BUILD_URL}}', buildUrl)
                .replace('{{STATUS_COLOR}}', '#d9534f')
                
            // Send email
            emailext(
                subject: subject,
                body: emailTemplate,
                to: recipients.join(','),
                mimeType: 'text/html'
            )
        }
    }
}
