```
uv venv --python 3.12.1 .error-hack-env

source .error-hack-env/bin/activate.fish

uv pip install ipykernel jupyterlab

python -m ipykernel install --user \
       --name error-hack-env-notebook \
       --display-name "Python 3.12 (error-hack-env-notebook)"

uv pip install --upgrade pip wheel

uv pip install \
  numpy scipy pandas scikit-learn scikit-image \
  matplotlib seaborn plotly tqdm rich ipywidgets

uv pip install \
  "transformers[torch]"    \
  accelerate>=0.28         \
  peft                     \
  bitsandbytes             \
  datasets                 \
  sentencepiece            \
  xformers                 \
  einops                   \
  trl                      \
  optimum                  \

uv pip install faiss-cpu # only 3.11 piton

uv pip freeze > requirements.txt

```
