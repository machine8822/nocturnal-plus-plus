package com.model;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * DataWriter saves User and InterviewQuestion objects to JSON files.
 * Extends DataConstants so it can detect JUnit and switch file paths.
 */
@SuppressWarnings("unchecked")
public class DataWriter extends DataConstants {

	/**
	 * Save all users to json/users.json
	 */
	public static boolean saveUsers(ArrayList<User> users) {
		JSONArray jsonUsers = new JSONArray();

		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			JSONObject userJSON = getUserJSON(user);
			jsonUsers.add(userJSON);
		}

		String path = getFileWritingPath(USER_FILE, USER_FILE_JUNIT);
		return writeJSONToFile(jsonUsers, path);
	}

	private static JSONObject getUserJSON(User user) {
		JSONObject userDetails = new JSONObject();

		userDetails.put("userId", user.getUserId().toString());
		userDetails.put("email", user.getEmail());
		userDetails.put("passwordHash", user.getPasswordHash());
		userDetails.put("firstName", user.getFirstName());
		userDetails.put("lastName", user.getLastName());
		userDetails.put("createdAt", user.getCreatedAt().toString());
		userDetails.put("lastLogin", user.getLastLogin() != null ? user.getLastLogin().toString() : null);
		userDetails.put("isAdmin", user.isAdmin());
		userDetails.put("isContributor", user.isContributor());

		JSONObject profile = new JSONObject();
		profile.put("school", user.getProfile().getSchool());
		profile.put("major", user.getProfile().getMajor());
		profile.put("gradYear", user.getProfile().getGradYear());
		profile.put("totalUpvotes", user.getProfile().getTotalUpvotes());
		profile.put("resumeURL", user.getProfile().getResumeURL());
		userDetails.put("profile", profile);

		return userDetails;
	}

	/**
	 * Save all interview questions to json/questions.json
	 */
	public static boolean saveQuestions(ArrayList<InterviewQuestion> questions) {
		JSONArray jsonQuestions = new JSONArray();

		for (int i = 0; i < questions.size(); i++) {
			InterviewQuestion question = questions.get(i);
			JSONObject questionJSON = getQuestionJSON(question);
			jsonQuestions.add(questionJSON);
		}

		String path = getFileWritingPath(QUESTION_FILE, QUESTION_FILE_JUNIT);
		return writeJSONToFile(jsonQuestions, path);
	}

	private static JSONObject getQuestionJSON(InterviewQuestion question) {
		JSONObject questionDetails = new JSONObject();

		questionDetails.put("questionId", question.getQuestionId().toString());
		questionDetails.put("title", question.getTitle());
		questionDetails.put("description", question.getDescription());
		questionDetails.put("difficulty", question.getDifficulty().toString());
		questionDetails.put("type", question.getType().toString());
		questionDetails.put("category", question.getCategory().toString());
		questionDetails.put("imageURL", question.getImageURL());
		questionDetails.put("authorId", question.getAuthorId() != null ? question.getAuthorId().toString() : null);
		questionDetails.put("totalAttempts", question.getTotalAttempts());
		questionDetails.put("totalSuccesses", question.getTotalSuccesses());
		questionDetails.put("createdAt", question.getCreatedAt().toString());
		questionDetails.put("lastUpdated", question.getLastUpdated().toString());

		JSONArray qComments = new JSONArray();
		List<Comment> questionComments = question.getComments();
		for (int i = 0; i < questionComments.size(); i++) {
			qComments.add(getCommentJSON(questionComments.get(i)));
		}
		questionDetails.put("comments", qComments);

		List<Section> sections = question.getSections();
		JSONArray sectionsJSON = new JSONArray();
		for (int i = 0; i < sections.size(); i++) {
			Section section = sections.get(i);
			JSONObject sectionJSON = getSectionJSON(section);
			sectionsJSON.add(sectionJSON);
		}
		questionDetails.put("sections", sectionsJSON);

		return questionDetails;
	}

	private static JSONObject getSectionJSON(Section section) {
		JSONObject sectionDetails = new JSONObject();

		sectionDetails.put("title", section.getTitle());
		sectionDetails.put("content", section.getBody());
		sectionDetails.put("type", section.getSectionType() != null ? section.getSectionType().toString() : null);
		sectionDetails.put("dataType", section.getDataType() != null ? section.getDataType().toString() : null);
		sectionDetails.put("imageURL", section.getImageURL());
		sectionDetails.put("expectedTimeComplexity", section.getExpectedTimeComplexity());
		sectionDetails.put("maxLinesOfCode", section.getMaxLinesOfCode());
		sectionDetails.put("timeLimitSeconds", section.getTimeLimitSeconds());

		JSONArray constraintsJSON = new JSONArray();
		List<String> constraints = section.getConstraints();
		for (int i = 0; i < constraints.size(); i++) {
			constraintsJSON.add(constraints.get(i));
		}
		sectionDetails.put("constraints", constraintsJSON);

		JSONArray examplesJSON = new JSONArray();
		List<String> examples = section.getExamples();
		for (int i = 0; i < examples.size(); i++) {
			examplesJSON.add(examples.get(i));
		}
		sectionDetails.put("examples", examplesJSON);

		JSONArray answersJSON = new JSONArray();
		List<Answer> answers = section.getAnswers();
		for (int i = 0; i < answers.size(); i++) {
			answersJSON.add(getAnswerJSON(answers.get(i)));
		}
		sectionDetails.put("answers", answersJSON);

		JSONArray commentsJSON = new JSONArray();
		List<Comment> comments = section.getComments();
		for (int i = 0; i < comments.size(); i++) {
			commentsJSON.add(getCommentJSON(comments.get(i)));
		}
		sectionDetails.put("comments", commentsJSON);

		return sectionDetails;
	}

	private static JSONObject getAnswerJSON(Answer answer) {
		JSONObject obj = new JSONObject();
		obj.put("answerId", answer.getAnswerId().toString());
		obj.put("codeSnippet", answer.getCodeSnippet());
		obj.put("explanation", answer.getExplanation());
		obj.put("upvoteCount", answer.getUpvoteCount());
		obj.put("downvoteCount", answer.getDownvoteCount());
		obj.put("authorId", answer.getAuthorId() != null ? answer.getAuthorId().toString() : null);
		obj.put("createdAt", answer.getCreatedAt() != null ? answer.getCreatedAt().toString() : null);

		JSONArray ansComments = new JSONArray();
		List<Comment> comments = answer.getComments();
		for (int i = 0; i < comments.size(); i++) {
			ansComments.add(getCommentJSON(comments.get(i)));
		}
		obj.put("comments", ansComments);
		return obj;
	}

	private static JSONObject getCommentJSON(Comment comment) {
		JSONObject obj = new JSONObject();
		obj.put("commentId", comment.getCommentId().toString());
		obj.put("text", comment.getText());
		obj.put("authorId", comment.getAuthorId() != null ? comment.getAuthorId().toString() : null);
		obj.put("timestamp", comment.getTimestamp() != null ? comment.getTimestamp().toString() : null);
		obj.put("isEdited", comment.isEdited());
		obj.put("upvoteCount", comment.getUpvoteCount());
		obj.put("downvoteCount", comment.getDownvoteCount());

		JSONArray repliesJSON = new JSONArray();
		List<Comment> replies = comment.getReplies();
		for (int i = 0; i < replies.size(); i++) {
			repliesJSON.add(getCommentJSON(replies.get(i)));
		}
		obj.put("replies", repliesJSON);
		return obj;
	}

	/**
	 * Returns the correct file path depending on whether running under JUnit.
	 *
	 * Normal run:  returns the regular file path (e.g. "json/users.json")
	 * JUnit test:  returns the path to the resource in src/test/resources
	 */
	private static String getFileWritingPath(String pathName, String junitPathName) {
		try {
			if (isJUnitTest()) {
				URI url = DataWriter.class.getResource(junitPathName).toURI();
				return url.getPath();
			} else {
				return pathName;
			}
		} catch (Exception e) {
			System.out.println("Difficulty getting resource path");
			return pathName;
		}
	}

	private static boolean writeJSONToFile(Object json, String filePath) {
		try (FileWriter file = new FileWriter(filePath)) {

			String jsonText;
			if (json instanceof JSONArray) {
				jsonText = ((JSONArray) json).toJSONString();
			} else if (json instanceof JSONObject) {
				jsonText = ((JSONObject) json).toJSONString();
			} else {
				jsonText = String.valueOf(json);
			}
			file.write(prettyPrintJson(jsonText));

			file.flush();

			return true;
		} catch (IOException e) {
			System.err.println("Error writing to file: " + filePath);
			System.err.println("Write failed: " + e.getMessage());
			return false;
		}
	}

	private static String prettyPrintJson(String rawJson) {
		if (rawJson == null || rawJson.isBlank()) {
			return "";
		}

		StringBuilder out = new StringBuilder();
		int indentLevel = 0;
		boolean inString = false;
		boolean escaping = false;

		for (int i = 0; i < rawJson.length(); i++) {
			char c = rawJson.charAt(i);

			if (inString) {
				out.append(c);
				if (escaping) {
					escaping = false;
				} else if (c == '\\') {
					escaping = true;
				} else if (c == '"') {
					inString = false;
				}
				continue;
			}

			switch (c) {
				case '"':
					inString = true;
					out.append(c);
					break;
				case '{':
				case '[':
					int next = findNextNonWhitespaceIndex(rawJson, i + 1);
					char closing = c == '{' ? '}' : ']';
					if (next >= 0 && rawJson.charAt(next) == closing) {
						out.append(c).append(closing);
						i = next;
					} else {
						out.append(c).append('\n');
						indentLevel++;
						appendIndent(out, indentLevel);
					}
					break;
				case '}':
				case ']':
					out.append('\n');
					indentLevel = Math.max(0, indentLevel - 1);
					appendIndent(out, indentLevel);
					out.append(c);
					break;
				case ',':
					out.append(c).append('\n');
					appendIndent(out, indentLevel);
					break;
				case ':':
					out.append(": ");
					break;
				default:
					if (!Character.isWhitespace(c)) {
						out.append(c);
					}
			}
		}

		return out.toString();
	}

	private static int findNextNonWhitespaceIndex(String text, int start) {
		for (int i = start; i < text.length(); i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return i;
			}
		}
		return -1;
	}

	private static void appendIndent(StringBuilder out, int indentLevel) {
		for (int i = 0; i < indentLevel; i++) {
			out.append("  ");
		}
	}

	public static void main(String[] args) {
	}
}
