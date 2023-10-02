package com.miniyus.friday.integration;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.ResultHandler;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * api documentation utils
 *
 * @author seongminyoo
 * @date 2023/09/08
 */
public class ApiDocumentUtils {
    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            prettyPrint()
        );
    }

    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
            prettyPrint()
        );
    }

    public static Snippet getDocumentSnippet(ResourceSnippetParameters parameters) {
        return resource(parameters);
    }

    public static ResultHandler getDocumentResultHandler(String identifier, ResourceSnippetParameters snippet) {
        return document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            getDocumentSnippet(snippet)
        );
    }
}
