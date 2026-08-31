# Git Conventions

> Adapted from the [SE-EDU Git conventions](https://se-education.org/guides/conventions/git.html), with one project-specific modification: **a commit message body is required for every commit**.

This convention forces full commit messages.

## Commit Message: Subject

Every commit must have a well-written commit message subject line.

- Try to limit the subject line to **50 characters** (hard limit: **72 characters**).
- Use the **imperative mood** in the subject line.
  - Good: `Add README.md`
  - Bad: `Added README.md`
  - Bad: `Adding README.md`
- Capitalise the first letter of the subject line.
  - Good: `Move index.html file to root`
  - Bad: `move index.html file to root`
- Do **not** end the subject line with a full stop.
  - Good: `Update sample data`
  - Bad: `Update sample data.`
- A `<scope>:` or `<category>:` may be added at the front when appropriate.
  - `Person class: Remove static imports`
  - `Main.java: Remove blank lines`
  - `bug fix: Add space after name`
  - `chore: Update release date`

## Commit Message: Body

**Every commit must have a commit message body.**

This is intentionally stricter than the original SE-EDU convention, which only requires bodies for non-trivial commits.

- Separate the subject from the body with a **blank line**.
- Wrap body lines at **72 characters**.
- Use blank lines to separate paragraphs.
- Use bullet points when they make the explanation clearer.
- Explain **WHAT** the commit changes and **WHY** the change is needed, rather than explaining **HOW** the implementation works.
- Give enough context for a reader to judge the purpose and rationale of the change without having to inspect the diff.
- Avoid duplicating information already explained by code comments in the same commit.
- If the body becomes excessively long, consider splitting the work into smaller, finer-grained commits.

### Recommended Body Structure

```text
{current situation} -- use present tense

{why it needs to change}

{what is being done about it} -- use imperative mood

{why it is done that way}

{any other relevant information}
```

Avoid words such as `currently` and `originally` when describing the present situation, as the context already implies this.

`Let's` may be used to introduce the section describing the change being made.

### Example

```text
Find command: Make matching case-insensitive

Find command is case-sensitive.

A case-insensitive find is more user-friendly because users cannot be
expected to remember the exact case of the keywords.

Let's:
- update the search algorithm to use case-insensitive matching
- add tests covering mixed-case search terms
```

## Branch Names

Follow these conventions to keep branch names consistent:

- Use a meaningful name containing relevant keywords in **kebab-case**.
  - Example: `refactor-ui-tests`
- If the branch relates to an issue, use:

  ```text
  issueNumber-some-keywords-from-issue-title
  ```

  Example:

  ```text
  1234-ui-freeze-error
  ```

## Commit Message Template

All commits should therefore follow this basic form:

```text
<Imperative subject, ideally <= 50 characters>

<Required body explaining what changes and why>
```

Example:

```text
Add validation for task dates

Task dates can be created with invalid values, which can result in
incorrect task information being stored.

Add validation before constructing dated tasks so that invalid dates are
rejected and the user receives an appropriate error message.
```

---

Source: SE-EDU, *Git conventions*: https://se-education.org/guides/conventions/git.html
