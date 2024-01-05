package com.meteormin.friday.document;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.restdocs.snippet.Snippet;

import java.util.ArrayList;
import java.util.Arrays;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * api documentation utils
 *
 * @author seongminyoo
 * @date 2023/09/08
 */
public class ApiDocumentUtils {
    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            prettyPrint());
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
            prettyPrint());
    }

    public static Snippet getDocumentSnippet(ResourceSnippetParameters parameters) {
        return resource(parameters);
    }

    public static RestDocumentationResultHandler getMockMvcDocumentResultHandler(String identifier,
        ResourceSnippetParameters parameters) {
        return MockMvcRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            getDocumentSnippet(parameters)
        );
    }

    public static RestDocumentationResultHandler getMockMvcDocumentResultHandler(String identifier,
        ResourceSnippetParameters parameters, Snippet... snippets) {
        var snippetList = new ArrayList<>(Arrays.asList(snippets));
        snippetList.add(getDocumentSnippet(parameters));

        return MockMvcRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            snippetList.toArray(new Snippet[0])
        );
    }

    public static RestDocumentationFilter getRestAssuredDocumentFilter(String identifier,
        ResourceSnippetParameters parameters) {
        return RestAssuredRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            getDocumentSnippet(parameters)
        );
    }

    public static RestDocumentationFilter getRestAssuredDocumentFilter(String identifier,
        ResourceSnippetParameters parameters, Snippet... snippets) {
        var snippetList = new ArrayList<>(Arrays.asList(snippets));
        snippetList.add(getDocumentSnippet(parameters));

        return RestAssuredRestDocumentationWrapper.document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            snippetList.toArray(new Snippet[0])
        );
    }
}
