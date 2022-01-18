package utn.frp.p3.application.middleware;

import java.util.NoSuchElementException;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import javafx.collections.ObservableList;
import utn.frp.p3.application.model.Whisky;

public class ServerAccess {
	private static String host = "localhost";
	private static int port = 8080;

	public static void getWhiskyStock(ObservableList<Whisky> whiskyData) {
		WebClient client = WebClient.create(Vertx.vertx());
		client.get(port, host, "/api/whiskies").send(ar -> {
			if (ar.succeeded()) {
				// Obtain response
				whiskyData.clear();
				HttpResponse<Buffer> response = ar.result();
				response.bodyAsJsonArray().forEach(whisky -> {
					JsonObject jw = (JsonObject) whisky;
					whiskyData.add(new Whisky(jw.getInteger("id"), jw.getString("name"), jw.getString("origin")));
				});
				System.out.println("Received response with status code " + response.statusCode());
				System.out.println(response.bodyAsJsonArray());
			} else {
				System.out.println("Something went wrong " + ar.cause().getMessage());
			}
		});
	}

	public static Future<Void> deleteWhisky(Whisky whisky) {
		Promise<Void> promise = Promise.promise();
		WebClient client = WebClient.create(Vertx.vertx());
		client.delete(port, host, "/api/whiskies/" + whisky.getId().intValue()).send(ar -> {
			if (ar.succeeded()) {
				System.out.println("Whisky " + whisky.getId().intValue() + " - " + whisky.getName().getValue()
						+ " exitosamente borrado");
				promise.complete();
			} else {
				promise.fail(new NoSuchElementException("Fallo de operación Delete Whisky " + ar.cause().getMessage()));
			}
		});
		return promise.future();
	}

	public static Future<Integer> insertWhisky(String name, String origin) {
		Promise<Integer> promise = Promise.promise();
		WebClient client = WebClient.create(Vertx.vertx());
		client.post(port, host, "/api/whiskies/")
				.sendJsonObject(new JsonObject().put("name", name).put("origin", origin), ar -> {
					if (ar.succeeded()) {
						// System.out.println("Whisky "+whisky.getId().intValue()+" -
						// "+whisky.getName().getValue()+" exitosamente borrado");
						System.out.println(ar.result().bodyAsString());
						promise.complete(ar.result().bodyAsJsonObject().getInteger("id"));
					} else {
						promise.fail(new NoSuchElementException(
								"Fallo de operación Delete Whisky " + ar.cause().getMessage()));
					}
				});
		return promise.future();
	}

	public static Future<Integer> updateWhisky(int id, String name, String origin) {
		Promise<Integer> promise = Promise.promise();
		WebClient client = WebClient.create(Vertx.vertx());
		client.put(port, host, "/api/whiskies/" + id)
				.sendJsonObject(new JsonObject().put("name", name).put("origin", origin), ar -> {
					if (ar.succeeded()) {
						// System.out.println("Whisky"+whisky.getId().intValue()+" -
						// "+whisky.getName().getValue()+" exitosamente borrado");
						System.out.println(ar.result().bodyAsString());
						promise.complete(id);
					} else {
						promise.fail(new NoSuchElementException(
								"Fallo de operación Delete Whisky " + ar.cause().getMessage()));
					}
				});
		return promise.future();
	}
}
