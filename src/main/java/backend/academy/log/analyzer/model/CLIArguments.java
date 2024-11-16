package backend.academy.log.analyzer.model;

public record CLIArguments(
    String path,
    String filterField,
    String filterValue,
    String dataFrom,
    String dataTo,
    String format
) {
}
