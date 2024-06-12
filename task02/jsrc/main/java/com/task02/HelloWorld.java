package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.task02.dto.HelloMessage;

@LambdaHandler(lambdaName = "hello_world",
               roleName = "hello_world-role",
               isPublishVersion = false,
               logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig
public class HelloWorld
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final int SC_OK = 200;
  public static final int SC_SERVER_ERROR = 500;
  public static final int BAD_REQUEST = 400;
  private final Gson gson = new Gson();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(
      APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent,
      Context context) {
    context.getLogger().log("Val: " + apiGatewayProxyRequestEvent.toString());
    context.getLogger().log("Val: Path:" + apiGatewayProxyRequestEvent.getPath());
    context.getLogger().log("Val: Method:" + apiGatewayProxyRequestEvent.getHttpMethod());
    if (apiGatewayProxyRequestEvent.getRequestContext() == null) {
      context.getLogger().log("Val: Context is null");
    } else {
      context.getLogger()
          .log("Val: Context.Path:" + apiGatewayProxyRequestEvent.getRequestContext().getPath());
      context.getLogger().log(
          "Val: Context.Method:" + apiGatewayProxyRequestEvent.getRequestContext().getHttpMethod());
    }
    try {

      if (!"/hello".equals(apiGatewayProxyRequestEvent.getPath())) {
        return new APIGatewayProxyResponseEvent()
            .withStatusCode(BAD_REQUEST)
            .withBody(gson.toJson(
                new HelloMessage(BAD_REQUEST,
                    "Bad request syntax or unsupported method. Request path: "
                    + apiGatewayProxyRequestEvent.getPath() + ". HTTP method: "
                    + apiGatewayProxyRequestEvent.getHttpMethod()
                )));
      }

      return new APIGatewayProxyResponseEvent()
          .withStatusCode(SC_OK)
          .withBody(gson.toJson(new HelloMessage("Hello from Lambda")));
    } catch (IllegalArgumentException exception) {
      context.getLogger().log(exception.getMessage());
      return new APIGatewayProxyResponseEvent()
          .withStatusCode(SC_SERVER_ERROR);
    }
  }
}
