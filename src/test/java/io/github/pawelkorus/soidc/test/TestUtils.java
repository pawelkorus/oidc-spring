package io.github.pawelkorus.soidc.test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static <T, N extends T> Answer<List<N>> setupListAnswer(N... values) {
        final List<N> someList = new ArrayList<N>();

        someList.addAll(Arrays.asList(values));

        Answer<List<N>> answer = new Answer<List<N>>() {
            public List<N> answer(InvocationOnMock invocation) throws Throwable {
                return someList;
            }
        };
        return answer;
    }

}
