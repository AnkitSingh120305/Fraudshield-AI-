package com.example

import com.example.engine.FraudRiskEngine
import com.example.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testDigitalArrestScamDetection_isHighRisk() {
        val dialogue = "This is Inspector Vikram from CBI Mumbai. You are placed under digital arrest right now. Transfer money to our safety account within 10 minutes or police will raid your house."
        val result = FraudRiskEngine.evaluateConversation(dialogue)

        assertEquals(RiskLevel.HIGH, result.riskLevel)
        assertTrue(result.score >= 70)
        assertTrue(result.detectedSignals.isNotEmpty())
    }

    @Test
    fun testHindiKYCOTPThreat_isHighRisk() {
        val dialogue = "मैं स्टेट बैंक से सीनियर मैनेजर बोल रहा हूँ। आपका खाता बंद कर दिया जाएगा। तुरंत वो ओटीपी बताओ वरना खाता ब्लॉक हो जाएगा।"
        val result = FraudRiskEngine.evaluateConversation(dialogue)

        assertEquals(RiskLevel.HIGH, result.riskLevel)
        assertTrue(result.score >= 68)
    }

    @Test
    fun testElectricityDisconnectionAnyDesk_isHighRisk() {
        val dialogue = "Your electricity power will be disconnected tonight at 8 PM. Download AnyDesk app and enter your UPI PIN immediately."
        val result = FraudRiskEngine.evaluateConversation(dialogue)

        assertEquals(RiskLevel.HIGH, result.riskLevel)
        assertTrue(result.score >= 68)
    }

    @Test
    fun testLegitimateDeliveryOTP_isLowRiskNoFalseAlarm() {
        val dialogue = "Hello sir, delivery agent at your gate with Amazon parcel. Please share the delivery OTP to hand over your box."
        val result = FraudRiskEngine.evaluateConversation(dialogue)

        // Sensitive word "OTP" present, but delivery context dampener without coercion or money transfer keeps it Low Risk
        assertEquals(RiskLevel.LOW, result.riskLevel)
        assertTrue(result.score < 32)
    }

    @Test
    fun testLegitimateBankInquiry_isLowRisk() {
        val dialogue = "Good morning sir, Priya calling from HDFC Bank regarding your pre-approved card upgrade. We never ask for your OTP or password over call."
        val result = FraudRiskEngine.evaluateConversation(dialogue)

        assertEquals(RiskLevel.LOW, result.riskLevel)
        assertTrue(result.score < 30)
    }
}
