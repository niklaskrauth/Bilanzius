package org.bilanzius.cli;

public record Question(String question, boolean repeatUntilValid, QuestionValidator validator, String defaultValue) {

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String question;
        private boolean repeatUntilValid = true;
        private String defaultValue = null;
        private QuestionValidator validator = (input) -> {
        };

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder validator(QuestionValidator validator) {
            this.validator = validator;
            return this;
        }

        public Builder repeatUntilValid(boolean repeatUntilValid) {
            this.repeatUntilValid = repeatUntilValid;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Question build() {
            if (this.question == null) {
                throw new IllegalArgumentException("Question must not be null.");
            }
            return new Question(this.question, this.repeatUntilValid, this.validator, this.defaultValue);
        }
    }

}
