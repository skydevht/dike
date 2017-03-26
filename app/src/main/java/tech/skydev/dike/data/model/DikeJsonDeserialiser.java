package tech.skydev.dike.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Hash Skyd on 3/26/2017.
 */


public class DikeJsonDeserialiser {
    public static class TitreDeserialiser implements JsonDeserializer<Titre> {

        @Override
        public Titre deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            final String id = jsonObject.getAsJsonPrimitive("id").getAsString();

            final String name = jsonObject.getAsJsonPrimitive("name").getAsString();

            final JsonArray jsonChaptersArray = jsonObject.getAsJsonArray("chapters");

            ArrayList<Chapter> chapters = null;

            if (jsonChaptersArray != null) {
                chapters = new ArrayList<>(0);
                for (JsonElement jsonElement :
                        jsonChaptersArray) {
                    Chapter chapter = context.deserialize(jsonElement, Chapter.class);
                    chapters.add(chapter);
                }
            }


            final Titre titre = new Titre();
            titre.setId(id);
            titre.setName(name);
            titre.setChapters(chapters);
            return titre;
        }
    }

    public static class ChapterDeserialiser implements JsonDeserializer<Chapter> {

        @Override
        public Chapter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            String id = null;

            final JsonElement idElement = jsonObject.get("id");
            if (idElement != null && !idElement.isJsonNull()) {
                id = idElement.getAsString();
            }

            String name = null;

            final JsonElement nameElement = jsonObject.get("name");
            if (nameElement != null && !nameElement.isJsonNull()) {
                name = nameElement.getAsString();
            }

            final JsonArray jsonSectionsArray = jsonObject.getAsJsonArray("sections");

            ArrayList<Section> sections = null;

            if (jsonSectionsArray != null) {
                sections = new ArrayList<>(0);
                for (JsonElement jsonElement :
                        jsonSectionsArray) {
                    Section section = context.deserialize(jsonElement, Section.class);
                    sections.add(section);
                }
            }

            return new Chapter(id, name, sections);
        }


    }

    public static class SectionDeserialiser implements JsonDeserializer<Section> {

        @Override
        public Section deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            String id = null;

            final JsonElement idElement = jsonObject.get("id");
            if (idElement != null && !idElement.isJsonNull()) {
                id = idElement.getAsString();
            }

            final JsonArray jsonArticleArray = jsonObject.getAsJsonArray("articles");

            ArrayList<Article> articles = null;

            if (jsonArticleArray != null) {
                articles = new ArrayList<>(0);
                for (JsonElement jsonElement :
                        jsonArticleArray) {
                    Article article = context.deserialize(jsonElement, Article.class);
                    articles.add(article);
                }
            }

            return new Section(id, articles);
        }
    }
}
