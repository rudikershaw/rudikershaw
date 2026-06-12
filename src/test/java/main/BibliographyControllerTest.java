package main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

@WebMvcTest(BibliographyController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "use.ssl=false")
public class BibliographyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBibliographyReturnsCorrectView() throws Exception {
        mockMvc.perform(get("/bibliography"))
            .andExpect(status().isOk())
            .andExpect(view().name("bibliography"));
    }
}
