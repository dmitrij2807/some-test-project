package dev.mesh.moneytransfer.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.mesh.moneytransfer.SpringBootApplicationTest;
import dev.mesh.moneytransfer.TestFileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class UsersTest extends SpringBootApplicationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void testGetUserPage() throws Exception {
    mockMvc.perform(get("/api/v1/user/page")
            .param("page", "0")
            .param("size", "10")
            .param("name", "sec"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            TestFileUtils.getResourceAsString("api/response/user_second_on_page.json"),
            true)
        );
  }
}