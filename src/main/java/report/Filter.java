package report;

/**
 * Filter to use on report.
 * @author deepak
 *
 */
public interface Filter {
	boolean filterIn(Row row);
}
