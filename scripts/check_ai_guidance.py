#!/usr/bin/env python3
from pathlib import Path
import sys

root = Path(__file__).resolve().parents[1]
required = [
    root / "AGENTS.md",
    root / ".codex/skills/learn-forge/SKILL.md",
    root / ".codex/skills/learn-forge/agents/openai.yaml",
    root / ".codex/skills/learn-forge/references/project-map.md",
    root / ".codex/skills/learn-forge/references/tokens-and-components.md",
    root / ".codex/skills/learn-forge/references/testing-and-release.md",
]
errors = [f"missing: {path.relative_to(root)}" for path in required if not path.is_file()]
placeholders = ("TODO", "TBD", "PLACEHOLDER", "replace me")
for path in required:
    if not path.is_file():
        continue
    text = path.read_text(encoding="utf-8")
    for placeholder in placeholders:
        if placeholder.lower() in text.lower():
            errors.append(f"placeholder '{placeholder}' in {path.relative_to(root)}")

skill = required[1].read_text(encoding="utf-8") if required[1].is_file() else ""
if not skill.startswith("---\n") or "name: learn-forge" not in skill or "description:" not in skill:
    errors.append("learn-forge SKILL.md frontmatter is incomplete")

if errors:
    print("AI guidance validation failed:\n" + "\n".join(errors), file=sys.stderr)
    raise SystemExit(1)
print("AI guidance validation passed")
