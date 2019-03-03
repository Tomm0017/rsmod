- Implement position multipliers for GPI decoding
- Instance map support
- Support to add player vars that will be serialized through plugins

Backlog:
- Implement support for reconnecting with no password required
- Add support for npc collision flags as npcs shouldn't be able to walk on top of one another
- Implement 'params' for NpcCombatDef to store information such as species
- Redo SimplePathFindingStrategy as current one is really bad and does not even support diagonal movement
- Item placeholder logic in ItemContainer should be handled via the bank plugin instead of ItemContainer itself
- Decouple World from Player