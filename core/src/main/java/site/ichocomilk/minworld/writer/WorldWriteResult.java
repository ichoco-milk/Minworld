package site.ichocomilk.minworld.writer;

public record WorldWriteResult(
    int amountSections,
    int amountChunks,
    int bufferSize
) {}