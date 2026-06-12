package main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

@WebMvcTest(GitBuildHookController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "use.ssl=false")
public class GitBuildHookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetRedirectsToGitHub() throws Exception {
        mockMvc.perform(get("/gitbuildhook"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testPostRedirectsToGitHub() throws Exception {
        mockMvc.perform(post("/gitbuildhook"))
            .andExpect(status().is3xxRedirection());
    }
}
