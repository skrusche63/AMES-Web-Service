import re, json


AMES_VOCAB_INPUT = r"C:\arwa\git\ames_vocab.txt"
AMES_VOCAB_OUTPUT = r"C:\arwa\git\ames_vocab_out.txt"
AMES_SYNS = r"C:\arwa\git\ames_syns.txt"

def convert(name):
    """
        >>> convert("RemoteLCRObject")
        ['Remote', 'LCR', 'Object']
    """
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1 \2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1 \2', s1).lower().split()

"""
    Each term entry is unique to its broader term
    Each term has a list of related terms
"""
vocab = {}
    
def main():
    for line in file(AMES_VOCAB_INPUT):
        parts = line.strip().split("/")
        module = parts[-1]
        parts = parts[:-1]
        moduleParts = convert(module)
        
        for (i, part) in enumerate(parts):
            # broader term
            broader = 'main concept'
            if i <> 0:
                broader = parts[i-1]
            # related terms
            if i+1 == len(parts):
                # end of package path gets module parts as related terms
                # substract own term and broader term from related
                related = set(moduleParts) - set([part, broader]) 
            else:
                related = [parts[i+1]]
                
            # update vocab if there are related terms
            if related:
                vocab.setdefault((part, broader), set()).update(related)
            
            # process module parts within its package context
            for mpart in moduleParts:
                # broader term
                if mpart == parts[-1]:
                    broader = parts[-2] # previous package part
                else:
                    broader = parts[-1] # last package part
                
                # related terms
                # substract own term and broader term from related
                related = set(moduleParts) - set([mpart, broader]) 

                #if not related:
                #    print "Line: %s | b: %s | parts %s" % (line, broader, moduleParts)
                
                # update vocab
                vocab.setdefault((mpart, broader), set()).update(related)
    
    # output
    file(AMES_VOCAB_OUTPUT, 'w').write(json.dumps(
            [dict([
                ('id', str(i)), 
                ('term', term), 
                ('broader', broader), 
                ('related', sorted(list([r for r in related if len(r)>1])))
                ])
                for i, ((term, broader), related) in enumerate(sorted(vocab.items()))
            ],
            sort_keys=True, indent=4
            )
        )
        

if __name__ == "__main__":
    main()