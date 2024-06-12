package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.Architecture;
import com.syndicate.deployment.model.ArtifactExtension;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import com.task02.dto.HelloMessage;

@LambdaHandler(lambdaName = "hello_world",
               roleName = "hello_world-role",
               isPublishVersion = false,
               /*aliasName = "${lambdas_alias_name}",*/
               layers = {"sdk-layer"},
               runtime = DeploymentRuntime.JAVA11,
               architecture = Architecture.ARM64,
               logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaLayer(
    layerName = "sdk-layer",
    libraries = {"lib/commons-lang3-3.14.0.jar", "lib/gson-2.10.1.jar"},
    runtime = DeploymentRuntime.JAVA11,
    architectures = {Architecture.ARM64},
    artifactExtension = ArtifactExtension.ZIP
)
@LambdaUrlConfig(
    authType = AuthType.NONE,
    invokeMode = InvokeMode.BUFFERED
)
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
