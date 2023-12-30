package com.miniyus.friday.integration;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.ResultHandler;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * api documentation utils
 *
 * @author miniyus
 * @date 2023/09/08
 */
public class ApiDocumentUtils {
    /**
     * Gets document request.
     *
     * @return the document request
     * @author miniyus
     * @date 2023/09/08
     */
    public static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
            prettyPrint()
        );
    }

    /**
     * Gets document response.
     *
     * @return the document response
     * @author miniyus
     * @date 2023/09/08
     */
    public static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
            prettyPrint()
        );
    }

    /**
     * Gets document snippet.
     *
     * @param parameters the parameters
     * @return the document snippet
     * @author miniyus
     * @date 2023/09/08
     */
    public static Snippet getDocumentSnippet(ResourceSnippetParameters parameters) {
        return resource(parameters);
    }

    /**
     * Gets document result handler.
     *
     * @param identifier the identifier
     * @param parameters the parameters
     * @return the document result handler
     * @author miniyus
     * @date 2023/09/08
     */
    public static ResultHandler getDocumentResultHandler(String identifier,
        ResourceSnippetParameters parameters) {
        return document(
            identifier,
            getDocumentRequest(),
            getDocumentResponse(),
            getDocumentSnippet(parameters)
        );
    }
}
