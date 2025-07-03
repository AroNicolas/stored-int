package school.hei.stored.file.hash;

import school.hei.stored.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
