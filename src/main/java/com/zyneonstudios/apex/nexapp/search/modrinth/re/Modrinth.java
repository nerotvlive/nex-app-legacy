package com.zyneonstudios.apex.nexapp.search.modrinth.re;

import masecla.modrinth4j.client.agent.UserAgent;
import masecla.modrinth4j.main.ModrinthAPI;
import masecla.modrinth4j.model.project.Project;

public class Modrinth {

    void main() {
        UserAgent agent = UserAgent.builder()
                .projectName("NEX App")
                .authorUsername("Zyneon Apex")
                .projectVersion("3.1.1")
                .build();

        String apiKey = "";
        ModrinthAPI client = ModrinthAPI.rateLimited(agent, apiKey);

        Project project = client.projects().get("gravestones").join();
        System.out.println(project.getTitle());
    }
}