package brigadier_legacy;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public final class CommandSource {

    public static final CharMatcher SUGGESTION_MATCH_PREFIX = CharMatcher.anyOf("._/");

    public static boolean shouldSuggest(String remaining, String candidate) {
        int i = 0;

        while (!candidate.startsWith(remaining, i)) {
            int j = SUGGESTION_MATCH_PREFIX.indexIn(candidate, i);
            if (j < 0) {
                return false;
            }

            i = j + 1;
        }

        return true;
    }

    public static CompletableFuture<Suggestions> suggestMatching(Iterable<String> candidates,
        SuggestionsBuilder builder) {
        String string = builder.getRemaining()
            .toLowerCase(Locale.ROOT);

        for (String string2 : candidates) {
            if (shouldSuggest(string, string2.toLowerCase(Locale.ROOT))) {
                builder.suggest(string2);
            }
        }

        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestPositions(String remaining,
        Collection<RelativePosition> candidates, SuggestionsBuilder builder, Predicate<String> predicate) {
        List<String> list = Lists.<String>newArrayList();
        if (Strings.isNullOrEmpty(remaining)) {
            for (CommandSource.RelativePosition relativePosition : candidates) {
                String string = relativePosition.x + " " + relativePosition.y + " " + relativePosition.z;
                if (predicate.test(string)) {
                    list.add(relativePosition.x);
                    list.add(relativePosition.x + " " + relativePosition.y);
                    list.add(string);
                }
            }
        } else {
            String[] strings = remaining.split(" ");
            if (strings.length == 1) {
                for (CommandSource.RelativePosition relativePosition2 : candidates) {
                    String string2 = strings[0] + " " + relativePosition2.y + " " + relativePosition2.z;
                    if (predicate.test(string2)) {
                        list.add(strings[0] + " " + relativePosition2.y);
                        list.add(string2);
                    }
                }
            } else if (strings.length == 2) {
                for (CommandSource.RelativePosition relativePosition2x : candidates) {
                    String string2 = strings[0] + " " + strings[1] + " " + relativePosition2x.z;
                    if (predicate.test(string2)) {
                        list.add(string2);
                    }
                }
            }
        }

        return suggestMatching(list, builder);
    }

    public static CompletableFuture<Suggestions> suggestColumnPositions(String remaining,
        Collection<CommandSource.RelativePosition> candidates, SuggestionsBuilder builder,
        Predicate<String> predicate) {
        List<String> list = Lists.<String>newArrayList();
        if (Strings.isNullOrEmpty(remaining)) {
            for (CommandSource.RelativePosition relativePosition : candidates) {
                String string = relativePosition.x + " " + relativePosition.z;
                if (predicate.test(string)) {
                    list.add(relativePosition.x);
                    list.add(string);
                }
            }
        } else {
            String[] strings = remaining.split(" ");
            if (strings.length == 1) {
                for (CommandSource.RelativePosition relativePosition2 : candidates) {
                    String string2 = strings[0] + " " + relativePosition2.z;
                    if (predicate.test(string2)) {
                        list.add(string2);
                    }
                }
            }
        }

        return suggestMatching(list, builder);
    }

    @Data
    public static class RelativePosition {

        public final String x;
        public final String y;
        public final String z;

        public static final RelativePosition ZERO_LOCAL = new RelativePosition("^", "^", "^");
        public static final RelativePosition ZERO_WORLD = new RelativePosition("~", "~", "~");
    }

}
