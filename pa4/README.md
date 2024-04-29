# CS6004 PA4

## Usage

- Run a test as follows:
    ```bash
    # execute from pa4 directory
    bash run.sh <command> <cp-directory> <classes-to-analyse-with-main-class-at-first-position>
    # e.g., bash run.sh c . Test4 Node4
    ```
- `<command>` must be one of `c` (counts) or `t` (times). The following are relevant for the file `openj9-openjdk-jdk8/openj9/runtime/vm/BytecodeInterpreter.inc`
    - For `c`, openj9 must have been compiled with `#define TRACE_NULLCHECK_TRANSITIONS`. This logs `ifnull` and `ifnonnull` and these are later counted. This is by default done on our submission repository
    - For `t`, the same must be `#undef TRACE_NULLCHECK_TRANSITIONS`. Further, other tracing also must be off for accurate timing (e.g., `#undef TRACE_TRANSITIONS`)
- This assumes that analysis, test, and soot files are located in `pa4` directory.
