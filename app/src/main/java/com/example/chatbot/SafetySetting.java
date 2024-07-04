package com.example.chatbot;

import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.HarmCategory;

public class SafetySetting {
    private HarmCategory harmCategory;

    private BlockThreshold blockThreshold;

    public SafetySetting(HarmCategory harmCategory, BlockThreshold blockThreshold) {
        this.harmCategory = harmCategory;
        this.blockThreshold = blockThreshold;
    }

    // Getters
    public HarmCategory getHarmCategory() {
        return harmCategory;
    }

    public BlockThreshold getBlockThreshold() {
        return blockThreshold;
    }

    // Setters can be added if needed
}
